package com.muju.note.launcher.app.healthy.contract;

import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface HealthyContract {

    interface View extends IView {
        void getHealthySuccess(List<VideoInfoDao> list);

        void getHealthyNull();

        void getHealthyEnd();
    }


    interface Presenter extends IPresenter<View> {
        void getHealthy(String name, int pageNum);
    }
}
