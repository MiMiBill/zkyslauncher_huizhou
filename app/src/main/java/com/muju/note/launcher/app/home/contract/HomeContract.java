package com.muju.note.launcher.app.home.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface HomeContract  {

    interface View extends IView {
        void getDate(String date);
        void getTime(String time);
        void getWeek(String week);
    }


    interface Presenter extends IPresenter<View> {
        void updateDate();
    }
}
