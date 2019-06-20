package com.muju.note.launcher.app.publicAdress.contract;

import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface PublicContract {

    interface View extends IView {
        void verfycode(String response);
        void verfycodeError();
        void doTask(TaskBean taskBean);
    }


    interface Presenter extends IPresenter<View> {
        void verfycode(String code,int adverId,String advertCode);
        void doTask(int userId,int advertId);
    }
}
