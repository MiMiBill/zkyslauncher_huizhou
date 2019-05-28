package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.util.log.LogUtil;


/**
 * 平板重启广播
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) { //开机启动完成后，要做的事情
            LogUtil.d("BootBroadcastReceiver onReceive(), Do thing!");
            LogUtil.d("重启上传日志");
//            if (!TimingUtils.getInstance().isOutOfTimingTime()) {


//            }
        }
    }
}
