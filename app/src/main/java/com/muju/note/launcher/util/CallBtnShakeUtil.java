package com.muju.note.launcher.util;

public class CallBtnShakeUtil {

    private static long preTime;

    public static boolean check()
    {
        boolean ret = (System.currentTimeMillis() -  preTime) > 600;
        preTime = System.currentTimeMillis();
        return  ret;
    }



}
