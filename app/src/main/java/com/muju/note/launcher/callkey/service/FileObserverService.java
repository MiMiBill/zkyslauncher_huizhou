package com.muju.note.launcher.callkey.service;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.callkey.Constant;
import com.muju.note.launcher.callkey.observer.KeyCodeFileObserver;
import com.muju.note.launcher.util.log.LogUtil;

public class FileObserverService extends Service {

    public static void start()
    {
        Intent intent = new Intent();
        intent.setClass(LauncherApplication.getContext(),FileObserverService.class);
        LauncherApplication.getContext().startService(intent);
    }

    private KeyCodeFileObserver keyCodeFileObserver;
    @Override
    public void onCreate() {
        super.onCreate();



//        startOberver();


        keyCodeFileObserver = new KeyCodeFileObserver(Constant.KEY_CODE_LOG_FILE_PATH, FileObserver.MODIFY);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("文件开始监听了");
        keyCodeFileObserver.startWatching();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        keyCodeFileObserver.stopWatching();
        LogUtil.d("停止监听文件");
    }
}
