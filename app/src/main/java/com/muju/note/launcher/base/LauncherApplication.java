package com.muju.note.launcher.base;

import android.app.Application;
import android.content.Context;

import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.OkGoUtil;
import com.muju.note.launcher.util.location.LocationUtil;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LauncherApplication extends Application {
    public String latitude = "";
    public String longitude = "";
    public String address = "";
    private static Context context;


    private static LauncherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        instance=this;

        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                // 日志初始化
                LogUtil.init();

                // litepal 初始化
                LitePal.initialize(instance);
                LitePalDb.addDb();
                LitePal.getDatabase();

                // okgo初始化
                OkGoUtil.initOkGo(instance);

                // wotv初始化
//                WoTvUtil.getInstance().initSDK(instance);
            }
        });

        // okgo初始化
        OkGoUtil.initOkGo(this);
        //初始化定位,心跳接口必须
        LocationUtil.initLocationOption(this);
    }

    public static Context getContext(){
        return context;
    }

    public static LauncherApplication getInstance(){
        return instance;
    }

}
