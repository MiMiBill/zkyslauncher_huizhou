package com.muju.note.launcher.app.clide.contract;

import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface ClideContract {

    interface View extends IView {
        void getClideSuccess(List<VideoInfoDao> list);
        void getClideNull();
        void getClideEnd();
        void getHeaderSuccess(List<VideoInfoDao> list);
    }


    interface Presenter extends IPresenter<View> {
        void getCilde(String name,int pageNum);
        void getHeader(String name);
    }
}
