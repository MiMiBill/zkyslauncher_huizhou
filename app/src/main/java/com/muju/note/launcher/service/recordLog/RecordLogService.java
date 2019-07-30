package com.muju.note.launcher.service.recordLog;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 *  平板当天操作日志
 */
public class RecordLogService {

    private static final String TAG="RecordLogService";

    public static RecordLogService recordLogService=null;
    public static RecordLogService getInstance(){
        if(recordLogService==null){
            recordLogService=new RecordLogService();
        }
        return recordLogService;
    }


    public void start(){
        // 开机1分钟内随机上传操作日志
        heartbeatTimer((long) (Math.random() * 1));
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

        Map<String, String> params = new HashMap();
        params.put("deviceBindingId", activeInfo.getBedId()+"");
        params.put("hospitalId", ""+activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
//        params.put("createDate", DateUtil.formartNowTimeToDate(System.currentTimeMillis()));
        OkGo.<String>post(UrlUtil.recordLog())
                .tag(UrlUtil.putHeartbeat())
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogFactory.l().i(""+response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        heartbeatTimer(30);
                    }
                });
    }

}
