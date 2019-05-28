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

    /**
     *  沃TV
     */
    public static final String WOTV_KEY="muju";
    public static final String WOTV_VALUE="mj785436";

    public static final String FILE_VIP_VIDEO = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "videoVIP.dat";
    public static final String FILE_VIP_GAME = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "gameVIP.dat";

}
