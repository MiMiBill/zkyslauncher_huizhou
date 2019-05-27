package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;


public interface VideoContract {

    interface View extends IView {
        void getColumnsSuccess(List<VideoColumnsDao> list);
        void getColumnsNull();
    }


    interface Presenter extends IPresenter<VideoContract.View> {
        void queryColumns();
    }

}
