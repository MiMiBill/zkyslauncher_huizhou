package com.muju.note.launcher.util;

import android.text.TextUtils;

import java.util.Calendar;

public class DateUtil {

    /**
     * 获取当前时分
     *
     * @return
     */
    public static String getNowHourAndMin() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return hour + ":" + min;
    }

    /**
     * 判断时间大小
     *
     * @param t1 当前时间 hh:mm
     * @param t2 设置的时间 hh:mm
     * @return
     */
    public static boolean checkTime(String t1, String t2) {
        if (TextUtils.isEmpty(t1) || t1.equals("")) {
            return false;
        }
        if (TextUtils.isEmpty(t2) || t2.equals("")) {
            return false;
        }
        int t1hour = Integer.parseInt(t1.split(":")[0]);
        int t1min = Integer.parseInt(t1.split(":")[1]);
        int t2hour = Integer.parseInt(t2.split(":")[0]);
        int t2min = Integer.parseInt(t2.split(":")[1]);
        int t1time = t1hour * 60 + t1min;
        int t2time = t2hour * 60 + t2min;
        return t1time >= t2time ? true : false;
    }
}
