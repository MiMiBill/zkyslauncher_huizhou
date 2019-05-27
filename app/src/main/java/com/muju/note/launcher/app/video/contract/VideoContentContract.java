package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;
import java.util.Map;


public interface VideoContentContract {

    interface View extends IView {
        void getVideoSuccess(List<VideoInfoDao> list);
        void getVideoNull();
        void getVideoend();
    }


    interface Presenter extends IPresenter<VideoContentContract.View> {
        void queryVideo(int columnId,String name,int pageNum);
        void queryVideo(int columnId,String name,int pageNum, String mapFilter);
    }

}
