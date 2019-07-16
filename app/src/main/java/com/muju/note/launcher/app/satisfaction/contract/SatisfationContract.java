package com.muju.note.launcher.app.satisfaction.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import org.json.JSONObject;

public interface SatisfationContract {

    interface View extends IView {
        void commitErrow();

        void getCommitSurveyData(String data);

        void getSurveyData(String data);
    }


    interface Presenter extends IPresenter<View> {
        void getCommitSurveyData(String sign, JSONObject jsonObject);

        void getSurveyData(String id, String sign);
    }
}
