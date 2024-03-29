package com.muju.note.launcher.push;


import android.content.Context;


import com.muju.note.launcher.util.log.LogUtil;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;


/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG="MyJPushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        LogUtil.i(TAG,"onTagOperatorResult Alias:" + jPushMessage.getAlias());
        LogUtil.i(TAG,"onTagOperatorResult tag:" + jPushMessage.getTags());
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
        LogUtil.i(TAG,"onTagOperatorResult");
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        LogUtil.i(TAG,"onAliasOperatorResult Alias:" + jPushMessage.getAlias());
        LogUtil.i(TAG,"onAliasOperatorResult tag:" + jPushMessage.getTags());
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        LogUtil.i(TAG,"onTagOperatorResult");
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
