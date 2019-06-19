package com.muju.note.launcher.app.bedsidecard.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface BedsideContract {

    interface View extends IView {
        void getDate(String date, String time, String week, String net, String netType);
    }


    interface Presenter extends IPresenter<View> {
        void updateDate();
    }
}
