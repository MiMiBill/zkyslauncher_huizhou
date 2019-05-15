package com.muju.note.launcher.service.operation;

import android.text.TextUtils;

import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.AppUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.system.SystemUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private static final String CLOSE_PAD = "closePad";
    private static final String OPEN_PAD = "openPad";

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
            //APP指定检查
//            isTopAppCheck();

            playConfig();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 执行配置文件操作
     */
    private void playConfig() throws Exception {
        List<PadConfigDao> configDaoList=LitePal.findAll(PadConfigDao.class);
        if(configDaoList==null||configDaoList.size()<=0){
            LogUtil.e(TAG,"平板配置信息为空，请检查配置");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        LogUtil.d("当前时间："+hour+":"+minute);
        for (PadConfigDao dao:configDaoList){
            switch (dao.getSort()){
                case "audio":

                    break;

                case "openVideo":

                    break;

                case "launch":
                    lockScreen(dao.getPadConfigs());
                    break;
            }
        }
    }

    /**
     * 执行开屏或关屏
     *
     * @param list
     * @throws Exception
     */
    private void lockScreen(List<PadConfigSubDao> list) throws Exception {
        String type = checkScreen(list);
        LogUtil.d(TAG,"开关屏类型："+type);
        if (TextUtils.isEmpty(type)) {
            return;
        }
        boolean isLock=SystemUtils.isLock();
        if(type.equals(OPEN_PAD)){
            if(isLock) {
                LogUtil.d(TAG,"当前处于开屏状态，无需在开屏");
            }else {
                LogUtil.d(TAG,"当前处于关屏状态，开启屏幕");
                Disposable di = Observable.timer(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                SystemUtils.turnOnScreen();
                                AppUtils.rebootPhone();
                            }
                        });


            }
        }
        if(type.equals(CLOSE_PAD)){
            if(isLock){
                LogUtil.d(TAG,"当前处于开屏状态，锁屏");
                Disposable di = Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                SystemUtils.screenOff();
                            }
                        });
            }else {
                LogUtil.d(TAG,"当前处于锁屏状态，无需在锁屏");
            }
        }
    }

    private String checkScreen(List<PadConfigSubDao> list) throws Exception {
        String nowDate = DateUtil.getNowHourAndMin();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                // 如果小于第一个item的时间，那么执行第一条的反操作
                if (!DateUtil.checkTime(nowDate, list.get(i).getActionTime())) {
                    if (TextUtils.equals(list.get(i).getType(), OPEN_PAD)) {
                        return CLOSE_PAD;
                    } else if (TextUtils.equals(list.get(i).getType(), CLOSE_PAD)) {
                        return OPEN_PAD;
                    }
                }
            }
            // 如果为最后一条item，那么直接执行当前操作
            if (i == list.size() - 1) {
                return list.get(i).getType();
            }

            // 如果当前时间大于item时间，并且小于下一item时间，执行当前item操作
            if (DateUtil.checkTime(nowDate, list.get(i).getActionTime()) && !DateUtil.checkTime(nowDate, list.get(i + 1).getActionTime())) {
                return list.get(i).getType();
            }
        }
        return null;
    }
}
