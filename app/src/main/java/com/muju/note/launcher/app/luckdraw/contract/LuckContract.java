package com.muju.note.launcher.app.luckdraw.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface LuckContract {
    interface View extends IView {
        void startSuccess(String data);
        void startFail();
    }


    interface Presenter extends IPresenter<LuckContract.View> {
        void startLuck(int id);
    }
}
