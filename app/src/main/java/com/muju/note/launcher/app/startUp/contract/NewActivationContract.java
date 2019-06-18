package com.muju.note.launcher.app.startUp.contract;

import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.startUp.db.ActivitionDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface NewActivationContract {

    interface View extends IView {
        void bindSuccess(ActivePadInfo.DataBean bean);
        void bindFail();
    }


    interface Presenter extends IPresenter<View> {
        void bindingDevice(String iccid);
    }
}
