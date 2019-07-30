package com.muju.note.launcher.util;

import android.text.TextUtils;

import java.text.ParsePosition;
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


    public static String timeStamp2Date(String seconds,String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
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
     *
     * @param date
     * @return
     */
    public static final boolean isValid(String date) {
        Calendar expireData = Calendar.getInstance();
        expireData.setTimeInMillis(Long.parseLong(date) * 1000);
        return expireData.getTimeInMillis() >= new Date().getTime();
    }

    /**
     * 判断时间是否相等
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isTimeRight(String t1, String t2) {
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
        return t1time == t2time ? true : false;
    }


    //时间格式化成时间戳
    public static long formartTime(String formartTime) {
        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(formartTime, new
                ParsePosition(0)).getTime() / 1000;
//        LogFactory.l().i("time===" + time);
        return time;
    }


    //时间戳格式化成时间
    public static String formartTimeToDate(long time) {
        String date= new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time*1000);
        return date.substring(0,4)+"年"+date.substring(5,7)+"月"+date.substring(8,10)+"日"+date.substring(11,16);
    }


    //时间戳格式化成时间
    public static String formartNowTimeToDate(long time) {
        String date= new SimpleDateFormat("yyyy-MM-dd").format(time);
        return date;
    }


    //获取倒计时
    public static String getTime(int tempTime) {
        if (tempTime > 0) {
            if (tempTime > 60 * 60 * 24) {
                int day = tempTime / (3600 * 24);
                int hour = tempTime % 3600 / 3600;
                int minitue = tempTime % 3600 / 60;
                return day + "天" + hour + "小时" + minitue + "分";
            } else {
                if (tempTime > 60 * 60) {
                    return tempTime / 3600 + "小时" + tempTime % 3600 / 60 + "分";
                } else {
                    return tempTime / 60 + "分";
                }
            }
        } else {
            int absTime = Math.abs(tempTime);
            if (absTime > 60 * 60 * 24) {
                int day = absTime / (3600 * 24);
                int hour = absTime % 3600 / 3600;
                int minitue = absTime % 3600 / 60;
                return "-" + day + "天"  + hour + "小时"  + minitue + "分";
            } else {
                if (absTime > 60 * 60) {
                    return  absTime / 3600 + "小时"  + absTime % 3600 / 60 + "分";
                } else {
                    return  absTime / 60 + "分";
                }
            }
        }
    }


    //获取倒计时天
    public static String getDay(int tempTime) {
        if (tempTime > 0) {
            if (tempTime > 60 * 60 * 24) {
                int day = tempTime / (3600 * 24);
                return day + "天";
            } else {
                return "0天";
            }
        } else {
            int absTime = Math.abs(tempTime);
            if (absTime > 60 * 60 * 24) {
                int day = absTime / (3600 * 24);
                return "-" + day + "天";
            } else {
                return "0天";
            }
        }
    }

    //获取倒计时小时时长
    public static String getHour(int tempTime) {
        int absTime = Math.abs(tempTime);
        if (absTime > 60 * 60 * 24) {
            int hour = absTime % 3600 / 3600;
            return  hour + "小时";
        } else {
            if (absTime > 60 * 60) {
                return  absTime / 3600 + "小时";
            } else {
                return  "0小时";
            }
        }
    }


    public static String diffTime(long diff) {
        String d =  0 + ":" + 0 + ":" + 0 ;
        String showHours="";
        String showMinutes="";
        String showSecond="";
        try {
            long hours = diff / (1000 * 60 * 60);
            long minutes = (diff - hours * (1000 * 60 * 60)) / (1000 * 60);
            long second = (diff - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);

            if(hours<10){
                showHours="0"+hours;
            }else {
                showHours=hours+" ";
            }
            if(minutes<10){
                showMinutes="0"+minutes;
            }else {
                showMinutes=" "+minutes;
            }
            if(second<10){
                showSecond="0"+second;
            }else {
                showSecond=" "+second;
            }

            d = showHours + ":" + showMinutes + ":" + showSecond;
            return d;
        } catch (Exception e) {
        }
        return d;
    }

}
