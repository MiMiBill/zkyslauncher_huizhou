package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;


public interface VideoPlayContract {

    interface View extends IView {
        void getComboList(String data);
        void orderCreate(String data);
    }


    interface Presenter extends IPresenter<VideoPlayContract.View> {
        void getComboList(int hospitalId,int deptId);
        void orderCreate(int comboId, String imei,int payType);
    }

}
