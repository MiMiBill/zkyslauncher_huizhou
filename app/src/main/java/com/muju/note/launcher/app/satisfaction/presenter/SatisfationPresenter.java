package com.muju.note.launcher.app.satisfaction.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.satisfaction.contract.SatisfationContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;

import org.json.JSONObject;

public class SatisfationPresenter extends BasePresenter<SatisfationContract.View> implements SatisfationContract.Presenter {


    @Override
    public void getCommitSurveyData(String sign, JSONObject jsonObject) {
        OkGo.<String>post(UrlUtil.getGetCommitSurveyData()).
                headers("Content-Type", "application/json;charset=utf-8").
                headers(Constants.PAD_SIGN, sign).
                upJson(jsonObject).
                tag(this).execute(new StringCallback() {


            @Override
            public void onSuccess(Response<String> response) {
                mView.getCommitSurveyData(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                mView.commitErrow();
            }

        });
    }

    @Override
    public void getSurveyData(String id,String sign) {
        OkGo.<String>get(String.format(UrlUtil.getGetSurveyData(), id)).
                headers(Constants.PAD_SIGN, sign)
                .tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                mView.getSurveyData(response.body());
            }

        });
    }
}
