package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;


public interface VideoLineContract {

    interface View extends IView {
        void getVideoSuccess(List<VideoInfoDao> list);
        void getVideoNull();
    }


    interface Presenter extends IPresenter<VideoLineContract.View> {
        void queryVideo();
    }

}
