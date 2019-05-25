package com.muju.note.launcher.app.home.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface HomeContract  {

    interface View extends IView {
        void getDate(String date,String time,String week);
    }


    interface Presenter extends IPresenter<View> {
        void updateDate();
    }
}
