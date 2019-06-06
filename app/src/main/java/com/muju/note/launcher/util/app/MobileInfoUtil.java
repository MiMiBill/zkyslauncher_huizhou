package com.muju.note.launcher.util.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class MobileInfoUtil {

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        //实例化TelephonyManager对象
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获取IMEI号
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        StringBuilder builder = new StringBuilder();
        String imei = telephonyManager.getDeviceId();
        builder.append(imei);
        return builder.toString();
    }


    public static boolean haveSIMCard(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        String imsi = manager.getSubscriberId(); // 取出IMSI
        if (imsi == null || imsi.length() <= 0) {
           return false;
        } else {
            return true;
        }
    }

    /**
     * 获取手机IMSI
     */
    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获取IMSI号
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        StringBuilder builder = new StringBuilder();
        String imsi = telephonyManager.getSubscriberId();
        builder.append(imsi);
        return builder.toString();
    }

    /**
     * 获取手机ICCID
     */
    public static String getICCID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        //todo kkjl提供的iccid只有19位，去掉了最后一位
        if (!TextUtils.isEmpty(telephonyManager.getSimSerialNumber()) && telephonyManager.getSimSerialNumber().length() > 19) {
            return telephonyManager.getSimSerialNumber().substring(0, 19);
        }
        return "";
    }

}
