package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.log.LogUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 关机时间日志
 */
public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("ShutdownReceiver：系统关机命令");

//        27196E386B875E76ADF700E7EA84E4C6EEE33DFA
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            LogUtil.d("ShutdownReceiver：系统关机命令");
            //example:写入文件
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(
                        android.os.Environment.getExternalStorageDirectory()
                                + File.separator + "SysLog.txt", true);
                fos.write(String.format("{\"shutdownTime\":\"%s\"\n}", FormatUtils.FormatDateUtil.formatDate(FormatUtils.FormatDateUtil.formatDate)).getBytes("utf-8"));
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
