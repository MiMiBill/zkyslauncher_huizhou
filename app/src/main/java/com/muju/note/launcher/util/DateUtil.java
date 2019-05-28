package com.muju.note.launcher.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
     * 获取当前时间
     *
     * @return
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间
     *
     * @param s
     * @return
     */
    public static String getDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat(s);
        Date d = new Date(System.currentTimeMillis());
        return format.format(d);
    }

    /**
     * 获取当前周几
     *
     * @return
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "";
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

    /**
     * 判断时间戳是否在有效期之内
     * @param date
     * @return
     */
    public static final boolean isValid(String date) {
        Calendar expireData = Calendar.getInstance();
        expireData.setTimeInMillis(Long.parseLong(date) * 1000);
        return expireData.getTimeInMillis() >= new Date().getTime();
    }
}
