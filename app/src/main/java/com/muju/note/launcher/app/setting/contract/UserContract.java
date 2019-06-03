package com.muju.note.launcher.app.setting.contract;

import com.muju.note.launcher.app.video.bean.UserBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface UserContract {

    interface View extends IView {
        void startQueryUser(UserBean bean);
        void qeryNotLogin();
        void QueryCode(String data);
    }


    interface Presenter extends IPresenter<View> {
        void startQueryUser();
        void getQueryCode();
    }
}
