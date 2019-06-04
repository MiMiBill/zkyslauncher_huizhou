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
    public static final String PAD_CONFIG_VOLUME_RATE = "PadConfigEntityVolume";

    /**
     *  沃TV
     */
    public static final String WOTV_KEY="muju";
    public static final String WOTV_VALUE="mj785436";

    /**
     *  七牛云数据库桶名
     */
    public static final String ZKYS_PAD_DB="zkyspaddb";

    public static final String FILE_VIP_VIDEO = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "videoVIP.dat";
    public static final String FILE_VIP_GAME = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "pay" + File.separator + "gameVIP.dat";

    //病人信息
    public static final String PATIENT="Patient";
    //    public static final String ZKYS_PAD_DB="appbucket";
    //广告存储
    public static final String ZKYS_ADVERTS="zkysadverts";
    public static final String PAD_SURVEY_ID = "pad_survey_id";

    public static final String PAD_CONFIG_OPEN_NETWORK_SPEEK = "PAD_CONFIG_OPEN_NETWORK_SPEEK";
    public static final String SHOW_LOG = "show_log";

    //各种广播
    public static final String ACTION_SHOW_STATUSBAR = "mid.systemui.show_statusbar";
    public static final String ACTION_HIDE_STATUSBAR = "mid.systemui.hide_statusbar";
    public static final String ACTION_SHOW_DEV = "mid.settings.show_dev";
    public static final String ACTION_HIDE_DEV = "mid.settings.hide_dev";
    public static final String ACTION_UMENG_WATCH_VIDEO = "umeng.watch.video";
}
