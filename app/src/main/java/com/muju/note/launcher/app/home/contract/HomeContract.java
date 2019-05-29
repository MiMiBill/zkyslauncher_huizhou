package com.muju.note.launcher.app.home.contract;

import android.content.Context;

import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface HomeContract  {

    interface View extends IView {
        void getDate(String date,String time,String week);
        void patientInfo(PatientResponse.DataBean entity);
        void notPatientInfo();
    }


    interface Presenter extends IPresenter<View> {
        void updateDate();
       void getPatientData(String padId, Context context);
    }
}
