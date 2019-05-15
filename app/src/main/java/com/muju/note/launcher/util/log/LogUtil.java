package com.muju.note.launcher.util.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


public class LogUtil {

    private static boolean isDebug = true;

    public static void init(){
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(true)  //（可选）是否显示线程信息。默认值true
//                .methodCount(2)         //（可选）要显示的方法行数。默认值2
//                .methodOffset(7)        //（可选）隐藏内部方法调用到偏移量。默认值5
//                .tag("zkysLauncher")   //（可选）每个日志的全局标记。默认PRETTY_LOGGER
//                .build();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static void d(Object obj) {
        if (isDebug) {
            Logger.d(obj);
        }
    }
    public static void d(String tag,Object... obj) {
        if (isDebug) {
            Logger.t(tag).d(obj);
        }
    }

    public static void i(String msg,Object... obj) {
        if (isDebug) {
            Logger.i(msg, obj);
        }
    }

    public static void i(String tag,String msg,Object... obj) {
        if (isDebug) {
            Logger.t(tag).i(msg,obj);
        }
    }

    public static void e(String msg,Object... obj) {
        if (isDebug) {
            Logger.e(msg, obj);
        }
    }

    public static void e(String tag,String msg,Object... obj) {
        if (isDebug) {
            Logger.t(tag).e(msg,obj);
        }
    }

    public static void e(Throwable throwable,String msg,Object... obj) {
        if (isDebug) {
            Logger.e(throwable, msg, obj);
        }
    }
}
