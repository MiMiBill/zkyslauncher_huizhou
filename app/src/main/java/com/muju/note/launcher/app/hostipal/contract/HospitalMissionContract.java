package com.muju.note.launcher.app.hostipal.contract;

import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface HospitalMissionContract {

    interface View extends IView {
        void getMission(List<MissionInfoDao> list);

        void getMissNull();
    }


    interface Presenter extends IPresenter<View> {
        void queryMiss();
    }
}
