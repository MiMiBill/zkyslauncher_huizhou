package com.muju.note.launcher.app.publicAdress.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface PublicContract {

    interface View extends IView {
        void verfycode(String response);
    }


    interface Presenter extends IPresenter<View> {
        void verfycode(String code,int adverId,String advertCode);
    }
}
