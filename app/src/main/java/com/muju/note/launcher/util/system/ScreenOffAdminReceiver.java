package com.muju.note.launcher.util.system;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.util.log.LogUtil;

/**
 * Receiver class which shows notifications when the Device Administrator status
 * of the application changes.
 */
public class ScreenOffAdminReceiver extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        LogUtil.e("ScreenOffAdminReceiver", "设备管理器使能");

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        LogUtil.e("ScreenOffAdminReceiver", "设备管理器没有使能");

    }

}