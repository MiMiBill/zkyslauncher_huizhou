package com.muju.note.launcher.app.userinfo.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface FeedBackContract {

    interface View extends IView {
        void post(String data);
        void postFail();
    }


    interface Presenter extends IPresenter<View> {
        void post(String content);
    }
}
