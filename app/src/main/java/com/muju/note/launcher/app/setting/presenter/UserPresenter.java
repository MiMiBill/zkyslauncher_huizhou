package com.muju.note.launcher.app.setting.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.setting.contract.UserContract;
import com.muju.note.launcher.app.video.bean.UserBean;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.user.UserUtil;

import org.greenrobot.eventbus.EventBus;

public class UserPresenter extends BasePresenter<UserContract.View> implements UserContract.Presenter {
    private UserListener listener;

    @Override
    public void startQueryUser() {
        OkGo.<BaseBean<UserBean>>get(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI
                (LauncherApplication.getContext()))
                .tag(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI(LauncherApplication
                        .getContext()))
                .execute(new JsonCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
//                        LogFactory.l().i("response==="+response.body().getData());
                        if (response.body().getData() == null) {
//                            mView.qeryNotLogin();
                            if (listener != null)
                                listener.qeryNotLogin();
                            return;
                        } else {
                            UserUtil.setUserBean(response.body().getData());
                            EventBus.getDefault().post(new UserInfoEvent(response.body().getData()));
                            if (listener != null)
                                listener.startQueryUser(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<UserBean>> response) {
                        super.onError(response);
//                        mView.qeryNotLogin();
                        if (listener != null)
                            listener.qeryNotLogin();
                    }
                });
    }

    @Override
    public void getQueryCode() {
        OkGo.<String>get(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI
                (LauncherApplication.getContext()))
                .tag(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI(LauncherApplication
                        .getContext()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (listener != null)
                            listener.QueryCode(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (listener != null)
                            listener.qeryNotLogin();
                    }
                });
    }


    public void setOnUserListener(UserListener listener) {
        this.listener = listener;
    }


    public interface UserListener {
        void startQueryUser(UserBean bean);

        void qeryNotLogin();

        void QueryCode(String data);
    }

}
