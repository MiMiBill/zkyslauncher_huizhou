package com.muju.note.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.muju.note.launcher.app.hostipal.service.MienService;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.service.config.ConfigService;
import com.muju.note.launcher.service.encyclope.EncyclopeService;
import com.muju.note.launcher.service.heartbeat.HeartBeatService;
import com.muju.note.launcher.service.location.LocationService;
import com.muju.note.launcher.service.operation.OneMinuteDisposable;
import com.muju.note.launcher.service.operation.SixHourDisposable;
import com.muju.note.launcher.service.updateversion.UpdateVersionService;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

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
        init();
        initByReboot();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"onStartCommand");
        return START_STICKY;
    }


    /**
     *  开机初始化操作
     */
    private void init(){

        // 1分钟心跳开始执行
        OneMinuteDisposable.getInstance().start();

        // 6小时心跳开始执行
        SixHourDisposable.getInstance().start();

        // 某些操作需要10分钟内执行
        Observable.timer((long) (Math.random() * 600), TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        // 上传开机心跳率
                        HeartBeatService.getInstance().uploadBootDate(1);
                        // 上传心跳信息
                        HeartBeatService.getInstance().start();
                    }
                });

    }

    /**
     *  根据自启动状态判定操作
     */
    private void initByReboot(){
        boolean rebootPhone = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        LogUtil.d(TAG,"自启动状态："+rebootPhone);
        if(rebootPhone){
            // 自启动状态，不做操作
        }else {
            // 非自启动状态，10分钟内初始化操作
            Observable.timer((long) (Math.random() * 600), TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            // 查询更新
                            UpdateVersionService.getInstance().start();
                            // 获取平板配置信息
                            ConfigService.getInstance().start();
                            // 获取定位信息
                            LocationService.getInstance().start();

                            //检查医院宣教视频数据
                            MienService.getInstance().getMienInfo();

                            // 检查影视数据
                            VideoService.getInstance().getVideoTopInfo();
                            VideoService.getInstance().getUpdateVideo();
                            VideoService.getInstance().getVideoCloumns();

                            //医疗百科科室
                            EncyclopeService.getInstance().getLately();

                            // 检查医院宣教数据
                            MissionService.getInstance().updateMission(1);
                        }
                    });
        }
        SPUtil.putBoolean(SpTopics.SP_REBOOT,false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OneMinuteDisposable.getInstance().stop();
        SixHourDisposable.getInstance().stop();
    }
}
