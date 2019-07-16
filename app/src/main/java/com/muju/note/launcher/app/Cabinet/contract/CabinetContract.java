package com.muju.note.launcher.app.Cabinet.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface CabinetContract {

    interface View extends IView {
        void getOrder(String data);

        void unLock(String data);

        void padNoOrder();

        void unLockFail();

        void returnBedFail();

        void reTurnBed(String data);

        void findByDid(String data);
    }


    interface Presenter extends IPresenter<View> {
        void getCabnetOrder();

        void unLock(String did);

        void returnBed(int id);

        void findByDid(String did);
    }
}
