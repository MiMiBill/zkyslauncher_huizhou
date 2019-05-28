package com.muju.note.launcher.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class UIUtils {

    public static final boolean checkHidePwd(Context context, String pwd) {
        String cryp = MobileInfoUtil.getICCID(context) + "," + MobileInfoUtil.getIMEI(context);
        String base64 = Base64.encodeToString(cryp.getBytes(), Base64.DEFAULT);
        String date = FormatUtils.FormatDateUtil.formatDate("yyyyMMdd");
        String key = String.valueOf((base64 + date).hashCode());
        String keyPwd = key.substring(key.length() - 6);
        LogUtil.d("keyPwd: %s", keyPwd);
        return TextUtils.equals(keyPwd, pwd);
    }


    //不限定参数拼接字符串
    public static String fun(String... msgs){
        String str="";
        for (int i = 0; i < msgs.length; i++) {
            str+=msgs[i]+",";
        }
        return str.substring(0,str.length()-1);
    }
}
