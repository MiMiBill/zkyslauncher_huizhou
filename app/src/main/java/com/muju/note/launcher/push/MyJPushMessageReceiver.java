package com.muju.note.launcher.push;


import android.content.Context;


import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;


/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
       /* if (jPushMessage.getTags().contains(Constant.JPUSH_TAG)) {
            LauncherApplication.getInstance().getSharedPreferences(Constant.JPUSH_SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(Constant.JPUSH_SP_SETTAG_KEY, true).commit();
        } else {
            LauncherApplication.getInstance().setJpushTag();
        }*/
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
