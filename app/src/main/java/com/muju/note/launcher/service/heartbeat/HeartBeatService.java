package com.muju.note.launcher.service.heartbeat;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.activeApp.entity.BaseEntity;
import com.muju.note.launcher.app.activeApp.entity.HeartbeatEntity;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.location.LocationService;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 *  心跳程序
 */
public class HeartBeatService {

    private static final String TAG="HeartBeatService";

    public static HeartBeatService heartBeatService=null;
    public static HeartBeatService getInstance(){
        if(heartBeatService==null){
            heartBeatService=new HeartBeatService();
        }
        return heartBeatService;
    }


    public void start(){
        // 开机10分钟内随机上传心跳信息
        heartbeatTimer((long) (Math.random() * 10));
    }

    private Disposable diHeartbeatTimer;
    /**
     * 心跳线程
     *
     * @param delay
     */
    private void heartbeatTimer(long delay) {
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
        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        if (activeInfo == null || activeInfo.getActivetion() != 1) {
            LogUtil.e(TAG,"激活信息为空，30分钟后重试");
            heartbeatTimer(30);
            return;
        }
        String padId = "" + activeInfo.getId();
        Map<String, String> params = new HashMap();
        params.put("content", String.format("{\"type\":\"heartbeat\",\"signal\":\"%s\"}", NetWorkUtil.getNetworkState(LauncherApplication.getContext()) +"  "+ NetWorkUtil.NETWORK_LEVEN));
        params.put("padId", padId);
        params.put("type", "1");
        params.put("hospitalId", ""+activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
        params.put("did", "" + MobileInfoUtil.getIMEI(LauncherApplication.getInstance()));
        params.put("versionCode", "" + BuildConfig.VERSION_CODE);
        params.put("longitude", LocationService.getInstance().longitude);
        params.put("latitude", LocationService.getInstance().latitude);
        params.put("address", LocationService.getInstance().address);
        Logger.json(new Gson().toJson(params));
        OkGo.<String>post(UrlUtil.putHeartbeat())
                .tag(UrlUtil.putHeartbeat())
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
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        heartbeatTimer(30);
                    }
                });
    }

    /**
     * 上传平板开关机时间
     *
     * @param type 1 开机 2关机
     */
    public void uploadBootDate(final int type) {
        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        Map<String, String> params = new HashMap();
        params.put("did", activeInfo.getCode());
        params.put("type", "" + type);
        params.put("hospitalId", ""+activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
        params.put("date", "" + DateUtil.getDate("yyyy-MM-dd"));
        params.put("createTime", "" + DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));
        OkGo.<String>post(UrlUtil.padLogInsert())
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtil.d("开关机日志上传成功：%s", response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d("开关机日志上传失败：%s", response.body());
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                uploadBootDate(type);
                            }
                        },1000*60*10);
                    }
                });
    }


}
