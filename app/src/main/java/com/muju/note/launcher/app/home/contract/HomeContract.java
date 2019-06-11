package com.muju.note.launcher.app.home.contract;

import android.content.Context;

import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface HomeContract {

    interface View extends IView {
        void getDate(String date, String time, String week,String net,String netType);

        void patientInfo(PatientResponse.DataBean entity);

        void notPatientInfo();

        void getVideoHisSuccess(List<VideoHisDao> list);

        void getVideoHisNull();

        void getVideoTopSuccess(List<VideoInfoDao> list);

        void getVideoTopNull();

        void getVideoTopImg(VideoInfoDao dao);

        void getTaskListSuccess();
    }


    interface Presenter extends IPresenter<View> {
        void updateDate();

        void getPatientData(String padId, Context context);

        void getVideoHis();

        void getTopVideo();

        void getTaskList(String userId,String hospitalId,String depId);
    }
}
