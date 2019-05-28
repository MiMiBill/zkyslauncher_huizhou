package com.muju.note.launcher.util;

import android.os.Environment;

import java.io.File;

public class Constants {

    /**
     *  平板前面参数
     */
    public static final String PAD_SIGN = "SIGN";

    //医疗百科下载压缩包
    public static final String ENCY_ZIP="ency_zip";

    public static final String SP_ENCY_UPDATE_TIME="sp_ency_update_time";
    public static final String SP_SIX_HOUR_TIME="sp_six_hour_time";

    public static final String HOST = "host";
    public static final String HOST_VERSION = "host_version";
    public static final String PAD_CONFIG_SIGNAL_LEVEL = "PAD_CONFIG_SIGNAL_LEVEL";
    public static final String PAD_CONFIG_SIGNAL_TYPE = "PAD_CONFIG_SIGNAL_TYPE";

    /**
     *  沃TV
     */
    public static final String WOTV_KEY="muju";
    public static final String WOTV_VALUE="mj785436";

    public static final String FILE_VIP_VIDEO = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "videoVIP.dat";
    public static final String FILE_VIP_GAME = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "gameVIP.dat";

}
