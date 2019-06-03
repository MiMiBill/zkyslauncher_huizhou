package com.muju.note.launcher.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.orhanobut.logger.Logger;

public class UIUtils {
    public static int mScreenWidth;
    public static int mScreenHeight;
    public static float mDensity;
    public static Context mContext;

    public static void init(Context context) {
        Logger.d("contextssssss", context);
        mContext = context;

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;
    }

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

    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        return (int) (mDensity * dp + 0.5f);
    }

    /**
     * 重启程序
     */
    public static void restartApplication() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(LauncherApplication.getContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LauncherApplication.getContext().startActivity(intent);
    }
}
