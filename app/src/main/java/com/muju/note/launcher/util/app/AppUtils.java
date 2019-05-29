package com.muju.note.launcher.util.app;

import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import java.io.IOException;

public class AppUtils {

    private static final String TAG="AppUtils";

    /**
     *
     *  关机
     */
    public static void rebootPhone(){
        String cmd = "reboot";
        try {
            SPUtil.putBoolean(SpTopics.SP_REBOOT,true);
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LogUtil.e(TAG,"error:"+e.getMessage());
        }
    }

}
