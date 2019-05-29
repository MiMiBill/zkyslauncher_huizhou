package com.muju.note.launcher.service.audio;

import android.app.IntentService;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;

import java.io.File;

/**
 * 音频播放服务
 */
public class PlayerAudioService extends IntentService {

    private static final String TAG="PlayerAudioService";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PlayerAudioService() {
        super("PlayerAudioService ");
        LogUtil.d(TAG,"PlayerAudioService:%s","构造");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LogUtil.d(TAG,"PlayerAudioService:%s", "onHandlerIntent()");
        String resource = intent.getStringExtra("resource");
        File file = new File(SdcardConfig.RESOURCE_FOLDER, resource);
        Uri uri = Uri.fromFile(file);
        AsyncPlayer asyncPlayer = new AsyncPlayer(null);
        asyncPlayer.play(this, uri, false, AudioManager.STREAM_MUSIC);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"PlayerAudioService:%s", "onCreate()");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.d(TAG,"PlayerAudioService:%s", "onStart()");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"PlayerAudioService:%s", "onDestroy()");
    }
}
