package com.muju.note.launcher.base;

import android.app.Application;
import android.content.Context;

import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.OkGoUtil;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;

public class LauncherApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

        // 日志初始化
        LogUtil.init();

        // okgo初始化
        OkGoUtil.initOkGo(this);

        LitePal.initialize(this);
        LitePal.getDatabase();

        LitePalDb.addDb();
    }

    public static Context getContext(){
        return context;
    }
}
