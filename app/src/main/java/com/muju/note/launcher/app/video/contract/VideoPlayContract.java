package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;


public interface VideoPlayContract {

    interface View extends IView {
        void getComboList(String data);
        void verfycode(String response);
        void verfycodeError();
        void setPayPackageList(String data);
        void setPayFail();
        void intervalSLOrder(String data);
    }


    interface Presenter extends IPresenter<VideoPlayContract.View> {
        void getComboList(int hospitalId,int deptId);
        void verfycode(String code);
        void setPayPackageList();
        void intervalSLOrder();
    }

}
