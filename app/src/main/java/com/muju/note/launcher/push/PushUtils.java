package com.muju.note.launcher.push;


import android.content.Context;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.SpTopics;


/**
 * 绑定推送信息等操作
 */
public class PushUtils {
    public static final String PUSH_BIND = "push_bind";
    public static final String PUSH_BIND_NEED = "push_bind_need";

    public static final String BIND_PATIENT = "bind_patient";
    public static final String BIND_PATIENT_NEED = "bind_patient_need";

    public static boolean needBind() {
        return LauncherApplication.getInstance().getSharedPreferences(PUSH_BIND, Context.MODE_PRIVATE).getBoolean(PUSH_BIND_NEED, true);
    }

    public static void setNeedBind(boolean isNeed) {
        LauncherApplication.getInstance().getSharedPreferences(PUSH_BIND, Context.MODE_PRIVATE).edit().putBoolean(PUSH_BIND_NEED, isNeed).commit();
    }


    public static boolean needInitPatient() {
        return LauncherApplication.getInstance().getSharedPreferences(BIND_PATIENT, Context.MODE_PRIVATE).getBoolean(BIND_PATIENT_NEED, true);
    }

    public static void setNeedBindPatient(boolean isNeed) {
        LauncherApplication.getInstance().getSharedPreferences(BIND_PATIENT, Context.MODE_PRIVATE).edit().putBoolean(BIND_PATIENT_NEED, isNeed).commit();
    }

    /**
     * 获取pad的id
     */
    public static int getPadId() {
        return LauncherApplication.getInstance().getSharedPreferences(SpTopics.SP_ZKY_PAD_LAUNCHER, Context.MODE_PRIVATE).getInt(SpTopics.SP_PAD_HAS_ACTIVE_ID, -1);
    }

    /**
     * 保存pad的id
     */
    public static boolean savePadId(int id) {
        return LauncherApplication.getInstance().getSharedPreferences(SpTopics.SP_ZKY_PAD_LAUNCHER, Context.MODE_PRIVATE).edit().putInt(SpTopics.SP_PAD_HAS_ACTIVE_ID, id).commit();
    }

    /**
     * 获取pad的jpushId是否成功保存到后台
     */
    public static boolean getPadJpushId() {
        return LauncherApplication.getInstance().getSharedPreferences(SpTopics.SP_ZKY_PAD_LAUNCHER, Context.MODE_PRIVATE).getBoolean(SpTopics.SP_PAD_JPUSH_ID, false);
    }

    /**
     * 保存pad的jpushId是否成功保存到后台
     */
    public static boolean savePadJpushId(boolean isSuccess) {
        return LauncherApplication.getInstance().getSharedPreferences(SpTopics.SP_ZKY_PAD_LAUNCHER, Context.MODE_PRIVATE).edit().putBoolean(SpTopics.SP_PAD_JPUSH_ID, isSuccess).commit();
    }
}
