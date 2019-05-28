package com.muju.note.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.activeApp.entity.BaseEntity;
import com.muju.note.launcher.app.activeApp.entity.HeartbeatEntity;
import com.muju.note.launcher.app.hostipal.service.MienService;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.encyclope.EncyclopeService;
import com.muju.note.launcher.service.http.ServiceHttp;
import com.muju.note.launcher.service.operation.OneMinuteDisposable;
import com.muju.note.launcher.service.operation.SixHourDisposable;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.sp.SPUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainService extends Service {

    private final static String TAG="MainService";
    private Disposable diHeartbeatTimer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG,"onCreate");
        init();
        boolean rebootPhone = SPUtil.getBoolean("rebootPhone");
        LogFactory.l().i(rebootPhone);
        if(rebootPhone){
            SPUtil.putBoolean("rebootPhone",false);
            init();
        }else {
            //自启动,根据上次心跳执行时间,重新启动心跳
            long currentTime = System.currentTimeMillis() / 1000;
            long sixHourTime = SPUtil.getLong(Constants.SP_SIX_HOUR_TIME);
            long delayTime= 6*60*60-(currentTime-sixHourTime);
            LogFactory.l().i("delayTime==="+delayTime);
            Observable.timer(delayTime,TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            SixHourDisposable.getInstance().start();
                        }
                    });
        }

        startdbService();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"onStartCommand");
        return START_STICKY;
    }


    //不管是怎么启动都必须启动检查更新db文件,开启心跳
    private void startdbService() {
        heartbeatTimer(0);
        // 1分钟心跳开始执行
        OneMinuteDisposable.getInstance().start();
        //检查医院宣教视频数据
        MienService.getInstance().start();
        // 检查影视数据
        VideoService.getInstance().start();

        //医疗百科科室
        EncyclopeService.getInstance().start();
    }


    /**
     *
     *  初始化操作
     */
    private void init(){
        // 10分钟内随机时间获取平板配置信息
        Observable.timer((long) (Math.random() * 10), TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ServiceHttp.getInstance().getPadConfigs();
                    }
                });
        // 6小时心跳开始执行
        SixHourDisposable.getInstance().start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OneMinuteDisposable.getInstance().stop();
        SixHourDisposable.getInstance().stop();
        RxUtil.closeDisposable(diHeartbeatTimer);
    }


    /**
     * 心跳线程
     *
     * @param delay
     */
    private void heartbeatTimer(long delay) {
        LogFactory.l().i("heartbeatTimer");
        RxUtil.closeDisposable(diHeartbeatTimer);
        diHeartbeatTimer = Observable.timer(delay, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        heartbeatHttp();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        LogFactory.l().e("throwable---"+throwable+throwable.getMessage());
                    }
                });
    }

    /**
     * 心跳程序
     */
    private void heartbeatHttp() {
        LogFactory.l().i("heartbeatHttp:");
        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        if (activeInfo == null || activeInfo.getActivetion() != 1) {
            heartbeatTimer(30);
            return;
        }

        String padId = "" + activeInfo.getId();
        Map<String, String> params = new HashMap();
        params.put("content", String.format("{\"type\":\"heartbeat\",\"signal\":\"%s\"}", SPUtil.getString(Constants.PAD_CONFIG_SIGNAL_TYPE) + SPUtil.getString(Constants.PAD_CONFIG_SIGNAL_LEVEL)));
        params.put("padId", padId);
        params.put("type", "1");
        params.put("hospitalId", ""+activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
        params.put("did", "" + MobileInfoUtil.getIMEI(LauncherApplication.getInstance()));
        params.put("versionCode", "" + BuildConfig.VERSION_CODE);
        if (!TextUtils.isEmpty(LauncherApplication.getInstance().latitude)) {
//            ，纬度：latitude，地址：address
            params.put("longitude", LauncherApplication.getInstance().longitude);
            params.put("latitude", LauncherApplication.getInstance().latitude);
            params.put("address", LauncherApplication.getInstance().address);
        }
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(this));
        OkGo.<String>post(UrlUtil.putHeartbeat())
                .tag(UrlUtil.putHeartbeat())
                .headers("SIGN", sign)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        String body = response.body();
                        BaseEntity<HeartbeatEntity> entity = gson.fromJson(body, new TypeToken<BaseEntity<HeartbeatEntity>>() {
                        }.getType());
                        if (entity.getCode() == 200 && entity.getData() != null && entity.getData().getNextTime() != 0) {
                            heartbeatTimer(entity.getData().getNextTime());
                        } else {
                            heartbeatTimer(30);
                        }
                        LogUtil.d("heartbeatHttp:请求成功" + body);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d("heartbeatHttp:请求失败" + response.body());
                        heartbeatTimer(30);
                    }
                });

    }

}
