package com.muju.note.launcher.app.sign.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.app.sign.contract.PriseContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;

import java.util.ArrayList;

public class PrisePresenter extends BasePresenter<PriseContract.View> implements PriseContract.Presenter {
    @Override
    public void getPointList(int userId) {
        OkGo.<BaseBean<ArrayList<PriseBean>>>post(UrlUtil.getPointList())
                .tag(this)
                .params("userId", userId)
                .execute(new JsonCallback<BaseBean<ArrayList<PriseBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<ArrayList<PriseBean>>> response) {
                        mView.setPrise(response.body().getData());
                    }

                    @Override
                    public void onError(Response<BaseBean<ArrayList<PriseBean>>> response) {
                        super.onError(response);
                    }
                });
    }
}
