package com.muju.note.launcher.app.video.util.wotv;

import android.util.Log;

import com.devbrackets.android.component.app.impl.MVPCustomHandler;
import com.unicom.common.VideoSdkConfig;
import com.unicom.common.callback.CommonResponseCallback;
import com.unicom.common.network.HttpUtils;

import java.util.Map;

/**
 * Created by henry on 2018/10/28
 * <p>
 * Description: Base View的配置，用于处理接口访问错误的情况
 */
public class BaseMVPHandler implements MVPCustomHandler {
    private static String TAG = "MVPHandler";

    public BaseMVPHandler(Object view){

    }

    @Override
    public void handleUnNormalStatus(Map<String, String> info, boolean isShowEmptyView) {
        if (info == null) {
            return;
        }
        Log.e(TAG, "错误状态：" + info.get(CommonResponseCallback.REQUEST_KEY_STATUS));
        Log.e(TAG, "错误信息：" + info.get(CommonResponseCallback.REQUEST_KEY_MESSAGE));

        //接口提示用户，需要重新登录了
        if (HttpUtils.isIdentityError(info.get(CommonResponseCallback.REQUEST_KEY_STATUS))) {
            if (VideoSdkConfig.getInstance().getOnLogoutListener() != null) {
                VideoSdkConfig.getInstance().getOnLogoutListener().onLogout(info.get(CommonResponseCallback.REQUEST_KEY_MESSAGE));
            }
        }
    }
}
