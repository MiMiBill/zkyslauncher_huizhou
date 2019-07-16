package com.muju.note.launcher.app.Cabinet.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface CabinetOrderContract {

    interface View extends IView {
        void unLock(String data);

        void unLockFail();

        void returnBedFail();

        void reTurnBed(String data);

        void findByDid(String data);
    }


    interface Presenter extends IPresenter<View> {
        void unLock(String id);

        void returnBed(int id);

        void findByDid(String did);
    }
}
