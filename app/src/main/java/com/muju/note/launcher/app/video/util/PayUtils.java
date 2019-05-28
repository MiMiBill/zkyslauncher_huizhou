package com.muju.note.launcher.app.video.util;


import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.file.FileIOUtils;


/**
 * 支付状态
 */
public class PayUtils {
    public static void setPaied(PayEntity entity) {
        Gson gson = new Gson();
        String data = gson.toJson(entity);
        data = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
        try {
            if (entity.getOrderType() == PayEntity.ORDER_TYPE_VIDEO) {
                FileIOUtils.writeFileFromString(Constants.FILE_VIP_VIDEO, data);
            } else if (entity.getOrderType() == PayEntity.ORDER_TYPE_GAME) {
                FileIOUtils.writeFileFromString(Constants.FILE_VIP_GAME, data);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 过期不要用
     *
     * @param orderType
     * @return
     */
    @Deprecated
    public static boolean hadPaied(int orderType) {
        PayEntity entity = getPayEntity(orderType);
        if (entity != null) {
            long vipTime = 24 * 60 * 60 * 1000 * entity.getDays();
            if ((System.currentTimeMillis() - entity.getPaiedTime()) < vipTime) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(int orderType) {
        PayEntity entity = getPayEntity(orderType);
        if (entity != null) {
            return DateUtil.isValid(entity.getExpireTime());
        }
        return false;
    }

    public static PayEntity getPayEntity(int orderType) {
        String data = null;
        try {
            if (orderType == PayEntity.ORDER_TYPE_VIDEO) {
                data = FileIOUtils.readFile2String(Constants.FILE_VIP_VIDEO);
            } else if (orderType == PayEntity.ORDER_TYPE_GAME) {
                data = FileIOUtils.readFile2String(Constants.FILE_VIP_GAME);
            }
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            data = new String(Base64.decode(data, Base64.DEFAULT), "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(data, PayEntity.class);
        } catch (Exception e) {
            e.getStackTrace();

        }
        return null;
    }



}
