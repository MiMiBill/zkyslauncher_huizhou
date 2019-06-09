package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ReplaceAddRemoveBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null && context.getPackageName().equals(data.getEncodedSchemeSpecificPart())) {
//                LogUtil.d("TAG", "更新安装成功.....");
//                Toast.makeText(context, "更新安装成功", Toast.LENGTH_LONG).show();
                // 重新启动APP
                Intent intentToStart = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                context.startActivity(intentToStart);
            }
        }
    }
}
