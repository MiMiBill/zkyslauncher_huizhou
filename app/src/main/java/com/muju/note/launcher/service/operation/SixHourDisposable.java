package com.muju.note.launcher.service.operation;

import android.annotation.SuppressLint;

import com.muju.note.launcher.service.config.ConfigService;
import com.muju.note.launcher.service.homemenu.HomeMenuService;
import com.muju.note.launcher.service.updateversion.UpdateVersionService;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sp.SPUtil;

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
        SPUtil.putLong(Constants.SP_SIX_HOUR_TIME,System.currentTimeMillis()/1000);
        //检查更新
        UpdateVersionService.getInstance().start();
        // 更新配置文件
        ConfigService.getInstance().getPadConfigs();
        // 更新首页列表
        HomeMenuService.getInstance().updateMenu(2);
    }


}
