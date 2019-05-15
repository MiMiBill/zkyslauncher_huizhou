package com.muju.note.launcher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.http.ServiceHttp;
import com.muju.note.launcher.service.operation.OneMinuteDisposable;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainService extends Service {

    private final static String TAG="MainService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG,"onCreate");
        init(LauncherApplication.getContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"onStartCommand");
        return START_STICKY;
    }

    /**
     *
     *  初始化操作
     * @param context
     */
    private void init(Context context){
        // 10分钟内随机时间获取平板配置信息
        Observable.timer((long) (Math.random() * 10), TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ServiceHttp.getInstance().getPadConfigs();
                    }
                });
        // 1分钟心跳开始执行
        OneMinuteDisposable.getInstance().start();
    }

}
