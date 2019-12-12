package com.muju.note.launcher.app.hostipal.contract;

import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface HospitalMienContract {

    interface View extends IView {
        void getMien(List<MienInfoDao> list);

        void getMienNull();

        void getHospitalMienVideoSuccess(List<VideoInfoDao> list);

        void getHospitalMienVideoNull();
    }

    interface Presenter extends IPresenter<View> {
        void queryMien();
        void getHospitalMienVideo(String name, int pageNum);
    }
}
