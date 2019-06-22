package com.muju.note.launcher.app.Cabinet.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.Cabinet.contract.CabinetContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;

public class CabinetPresenter extends BasePresenter<CabinetContract.View> implements CabinetContract.Presenter {

    @Override
    public void getCabnetOrder() {
        OkGo.<String>get(String.format(UrlUtil.getCabnetOrder(),MobileInfoUtil.getIMEI(LauncherApplication.getContext())))
                .tag(UrlUtil.getCabnetOrder() + MobileInfoUtil.getIMEI(LauncherApplication.getContext()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mView.getOrder(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.padNoOrder();
                    }
                });
    }

    @Override
    public void unLock(String did) {
        OkGo.<String>post(UrlUtil.unLock())
                .tag(UrlUtil.unLock())
                .params("did",did)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mView.unLock(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.unLockFail();
                    }
                });
    }

    @Override
    public void returnBed(int id) {
        OkGo.<String>post(UrlUtil.returnBed())
                .tag(UrlUtil.returnBed())
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mView.reTurnBed(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.returnBedFail();
                    }
                });
    }

    @Override
    public void findByDid(String did) {
        OkGo.<String>post(UrlUtil.findByDId())
                .tag(UrlUtil.findByDId())
                .params("did",did)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mView.findByDid(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.unLockFail();
                    }
                });
    }


}
