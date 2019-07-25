package com.muju.note.launcher.app.orderfood.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.orderfood.contract.PayContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class PayPresenter extends BasePresenter<PayContract.View> implements PayContract.Presenter {

    @Override
    public void queryPay(int orderId) {
        OkGo.<String>post(UrlUtil.orderTableStatus())
                .tag(this)
                .params("id", orderId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        String body = response.body();
                        mView.queryPay(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                    }
                });
    }

    //取消订单
    @Override
    public void canclePay(int orderId) {
        OkGo.<String>post(UrlUtil.cancleTable())
                .tag(this)
                .params("id", orderId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        String body = response.body();
                        mView.queryPay(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                    }
                });
    }
}
