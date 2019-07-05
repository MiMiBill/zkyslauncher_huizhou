package com.muju.note.launcher.service.operation;

import com.muju.note.launcher.service.config.ConfigService;
import com.muju.note.launcher.service.updatedata.UpdateDataService;
import com.muju.note.launcher.service.uploaddata.UpLoadDataService;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 *  1分钟心跳类
 */
public class OneMinuteDisposable {

    private final static String TAG="OneMinuteDisposable";

    public static OneMinuteDisposable oneMinute = null;

    public static OneMinuteDisposable getInstance() {
        if (oneMinute == null) {
            oneMinute = new OneMinuteDisposable();
        }
        return oneMinute;
    }


    private Disposable diTimer;

    public void start() {
        //时间矫正偏移
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        Observable.timer(60 - second, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        RxUtil.closeDisposable(diTimer);
                        diTimer = Observable.interval(1, TimeUnit.MINUTES)
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        LogUtil.d("一分钟线程执行");
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
                });
    }

    private void runStruct() {
        try {
            // 查看配置文件是否需要有执行的操作
            ConfigService.getInstance().playConfig();

            // 获取信号强度
//            NetWorkUtil.getCurrentNetDBM(LauncherApplication.getContext());
//            NetWorkUtil.getSignalStrength(LauncherApplication.getContext());

            // 检查是否需要更新本地数据
            UpdateDataService.getInstance().start();

            // 检查是否需要上传统计数据
            UpLoadDataService.getInstance().start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        RxUtil.closeDisposable(diTimer);
    }

}
