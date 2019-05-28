package com.muju.note.launcher.util;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.topics.FileTopics;
import com.muju.note.launcher.util.file.FileIOUtils;
import com.orhanobut.logger.Logger;

public class ActiveUtils {

    public static void setActiveInfo(ActivePadInfo.DataBean entity) {
        Gson gson = new Gson();
        String data = gson.toJson(entity);
        data = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
        try {
            FileIOUtils.writeFileFromString(FileTopics.FILE_ACTIVE_INFO, data);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static String getRequestHeader() {
        ActivePadInfo.DataBean entity = getPadActiveInfo();
        return entity == null ? "" : entity.getPad();
    }

    public static ActivePadInfo.DataBean getPadActiveInfo() {
        try {
            String data = FileIOUtils.readFile2String(FileTopics.FILE_ACTIVE_INFO);
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            data = new String(Base64.decode(data, Base64.DEFAULT), "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(data, ActivePadInfo.DataBean.class);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public static String getPhoneNumber() {
        String phone = "13800138000";
        ActivePadInfo.DataBean entity = getPadActiveInfo();
        if (entity != null) {
            String simNumber = entity.getSimMobile();
            if (!TextUtils.isEmpty(simNumber) && simNumber.length() == 11) {
                return simNumber;
            }
        }
        Logger.d("phone:%s", phone);
        return phone;
    }

}
