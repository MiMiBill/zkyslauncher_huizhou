package com.muju.note.launcher.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.callkey.service.CallKeyService;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.OkGoUtil;
import com.muju.note.launcher.util.carsh.CrashHandler;
import com.muju.note.launcher.util.log.LogUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;

public class LauncherApplication extends Application {
    public String latitude = "";
    public String longitude = "";
    public String address = "";
    private static Context context;


    private static LauncherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;

        sendBroadcast(new Intent("mid.systemui.hide_statusbar"));
        sendBroadcast(new Intent("mid.settings.hide_dev"));
        sendFullScreenBroadcast();

        // 日志初始化
        LogUtil.init();
        // litepal 初始化
        LitePal.initialize(instance);
        LitePalDb.addDb();
        LitePal.getDatabase();

        // okgo初始化
        OkGoUtil.initOkGo(instance);

        // 异常信息捕获
        CrashHandler.getInstance().init(this);

        CrashReport.initCrashReport(this, "dcea390c6e", false);

        // 初始化极光推送
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);

        CallKeyService.start();

        // wotv初始化
//                WoTvUtil.getInstance().initSDK(instance);
    }

    private void sendFullScreenBroadcast() {
        //todo 小鲸鱼专用全屏广播
        Intent hideNavigIntent = new Intent();
        hideNavigIntent.setAction("android.intent.action.sendkey");
        hideNavigIntent.putExtra("keycode", 1239);
        hideNavigIntent.putExtra("state", 1);
        sendBroadcast(hideNavigIntent);
    }

    public static Context getContext() {
        return context;
    }

    public static LauncherApplication getInstance() {
        return instance;
    }

}
