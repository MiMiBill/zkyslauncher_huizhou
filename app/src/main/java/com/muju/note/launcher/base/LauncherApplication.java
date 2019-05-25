package com.muju.note.launcher.base;

import android.app.Application;
import android.content.Context;

import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.OkGoUtil;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;

public class LauncherApplication extends Application {

    private static Context context;


    private static LauncherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        instance=this;

        // 日志初始化
        LogUtil.init();

        // litepal 初始化
        LitePal.initialize(this);
        LitePalDb.addDb();
        LitePal.getDatabase();

        // okgo初始化
        OkGoUtil.initOkGo(this);

    }

    public static Context getContext(){
        return context;
    }

    public static LauncherApplication getInstance(){
        return instance;
    }

}
