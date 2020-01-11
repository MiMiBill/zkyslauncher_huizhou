package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.muju.note.launcher.app.setting.event.VolumeEvent;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 音量改变监听
 */
public class VolumeBroadcastReceiver extends BroadcastReceiver {
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

    @Override
    public void onReceive(Context context, Intent intent) {
//        LogUtil.v("音量改变:%s", intent.getAction());
        //媒体音量改变才通知
        if (VOLUME_CHANGED_ACTION.equals(intent.getAction())
                && (intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC)) {
//            int current = SystemUtils.getCurrentVolume(context);
//            long rateVolume = SPUtil.getLong(SpTopics.PAD_CONFIG_VOLUME_RATE);
//            rateVolume = rateVolume == -1 ? 60 : rateVolume;
//            int volume = (int) (SystemUtils.getMaxVolume(context) / 100D * rateVolume);
////            LogFactory.l().i("rateVolume==="+rateVolume+"===current==="+ current+"===volume==="+volume);
//            if (current > volume) {
//                SystemUtils.setVolume(context, (int) rateVolume);
//            }
//            EventBus.getDefault().post(new VolumeEvent());
        }
    }
}
