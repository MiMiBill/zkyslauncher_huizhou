package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;

/**
 * 关机时间日志
 */
public class PowerDisconnectedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogFactory.l().i("系统断电");
        if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            LogFactory.l().i("系统断电");
//            SPUtil.putBoolean("rebootPhone",true);
        }
    }
}
