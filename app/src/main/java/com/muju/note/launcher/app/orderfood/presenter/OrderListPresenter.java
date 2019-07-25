package com.muju.note.launcher.app.orderfood.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.app.orderfood.contract.OrderListContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.List;

public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {

    @Override
    public void tabList(int bedid) {
        OkGo.<BaseBean<List<OrderListBean>>>post(UrlUtil.tableList())
                .tag(this)
                .params("bedId", bedid)
                .params("pageSize", 100)
                .execute(new JsonCallback<BaseBean<List<OrderListBean>>>() {
                             @Override
                             public void onSuccess(Response<BaseBean<List<OrderListBean>>> response) {
                                 if (mView == null) {
                                     LogUtil.e("mView为空");
                                     return;
                                 }
                                 mView.tabList(response.body().getData());
                             }
                         }

                );
    }
}
