package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.muju.note.launcher.callkey.bean.CallKeyInfo;
import com.muju.note.launcher.callkey.event.CallKeyDownEvent;
import com.muju.note.launcher.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

public class BtnCallPressedBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BtnCallPressedBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG,"Call按键广播：" + intent.getAction());

        if (CallKeyInfo.getsInstance().isDaytimeMode() && CallKeyInfo.getsInstance().isEnableSwitchScreen())
        {
            EventBus.getDefault().post(new CallKeyDownEvent(false,"Call按键，来自广播"));
        }

    }
}
