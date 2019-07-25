package com.muju.note.launcher.app.orderfood.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.muju.note.launcher.app.orderfood.contract.OrderPayContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

import org.json.JSONObject;

public class OrderPayPresenter extends BasePresenter<OrderPayContract.View> implements OrderPayContract.Presenter {

    @Override
    public void orderCreate(JSONObject jsonObject) {
        OkGo.post(UrlUtil.orderTableCreate()).upJson(jsonObject).execute(new Callback<Object>() {
            @Override
            public String convertResponse(okhttp3.Response response) throws Throwable {
                String body = response.body().string();
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return null;
                }
                mView.orderCreate(body);
                return body;
            }

            @Override
            public void onStart(Request<Object, ? extends Request> request) {

            }

            @Override
            public void onSuccess(Response<Object> response) {
                /*if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                String body = (String) response.body();
                LogFactory.l().i("body==="+body);
                mView.orderCreate(body);*/
            }

            @Override
            public void onCacheSuccess(Response<Object> response) {
                /*if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                String body = (String) response.body();
                mView.orderCreate(body);*/
            }

            @Override
            public void onError(Response<Object> response) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                mView.orderError();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void uploadProgress(Progress progress) {

            }

            @Override
            public void downloadProgress(Progress progress) {

            }

        });
    }
}
