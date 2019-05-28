package com.muju.note.launcher.app.startUp.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface ActivationContract {

    interface View extends IView {
        void bindSuccess(String data);
        void bindFail();
    }


    interface Presenter extends IPresenter<View> {
        void bindingDevice(String iccid);
    }
}
