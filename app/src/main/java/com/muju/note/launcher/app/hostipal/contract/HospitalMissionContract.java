package com.muju.note.launcher.app.hostipal.contract;

import com.muju.note.launcher.app.hostipal.db.DepartmentInfoDao;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface HospitalMissionContract {

    interface View extends IView {

        void getMission(List<MissionInfoDao> list);

        void getMissNull();

        void getMissionVideoSuccess(List<VideoInfoDao> list);

        void getMissionVideoNull();

        void getDepartmentInfosSuccess(List<DepartmentInfoDao> list);

        void getDepartmentInfosNull();
    }


    interface Presenter extends IPresenter<View> {
        void queryMiss(int hospitalId,int deptId);
        void getMissionVideo(String name, int pageNum);
        void queryDepartmentInfos(int hospitalId);
    }
}
