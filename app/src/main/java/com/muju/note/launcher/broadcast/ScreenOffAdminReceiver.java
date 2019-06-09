package com.muju.note.launcher.broadcast;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.util.log.LogUtil;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ScreenOffAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        LogUtil.e( "设备管理器使能");

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        LogUtil.e("设备管理器没有使能");

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

}
