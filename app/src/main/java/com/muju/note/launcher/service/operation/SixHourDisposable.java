package com.muju.note.launcher.service.operation;

import android.annotation.SuppressLint;
import android.content.Context;

import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 一分钟执行一次
 * 检测开关屏
 */
public class SixHourDisposable {

    private static final String TAG="SixHourDisposable";

    public static SixHourDisposable oneMinute = null;
    private Disposable diTimer;


    public static SixHourDisposable getInstance() {
        if (oneMinute == null) {
            oneMinute = new SixHourDisposable();
        }
        return oneMinute;
    }

    public SixHourDisposable() {

    }

    @SuppressLint("CheckResult")
    public void start() {
        RxUtil.closeDisposable(diTimer);
        diTimer = Observable.interval(6, TimeUnit.HOURS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d(TAG,"六小时线程执行");
                        runStruct();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable, throwable.getMessage());
                        start();
                    }
                });
    }

    public void stop() {
        RxUtil.closeDisposable(diTimer);
    }

    private void runStruct() {

    }


}
