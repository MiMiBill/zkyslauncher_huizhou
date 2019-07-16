package com.muju.note.launcher.app.publicui.contract;

import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface ProtectionContract {

    interface View extends IView {

        void getLockBananaList(List<AdvertsCodeDao> list);

        void getLockBananaNull();
    }


    interface Presenter extends IPresenter<View> {
        void getLockBananaList(String code);

    }
}
