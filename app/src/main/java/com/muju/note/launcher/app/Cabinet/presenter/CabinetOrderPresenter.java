package com.muju.note.launcher.app.Cabinet.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.Cabinet.contract.CabinetOrderContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class CabinetOrderPresenter extends BasePresenter<CabinetOrderContract.View> implements CabinetOrderContract.Presenter {

    @Override
    public void unLock(String did) {
        OkGo.<String>post(UrlUtil.unLock())
                .tag(UrlUtil.unLock())
                .params("did", did)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.unLock(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.unLockFail();
                    }
                });
    }

    @Override
    public void returnBed(int id) {
        OkGo.<String>post(UrlUtil.returnBed())
                .tag(UrlUtil.returnBed())
                .params("id", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.reTurnBed(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.returnBedFail();
                    }
                });
    }

    @Override
    public void findByDid(String did) {
        OkGo.<String>post(UrlUtil.findByDId())
                .tag(UrlUtil.findByDId())
                .params("did", did)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.findByDid(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.unLockFail();
                    }
                });
    }
}
