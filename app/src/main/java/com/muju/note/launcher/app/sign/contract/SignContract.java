package com.muju.note.launcher.app.sign.contract;

import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface SignContract {
    interface View extends IView {
        void chesignStatus(SignStatusBean bean);
        void checkSign(SignBean data);
    }


    interface Presenter extends IPresenter<SignContract.View> {
        void checkSign(int id);
        void checkSignStatus(int id);
        void doTask(int userId,int advertId);
    }
}
