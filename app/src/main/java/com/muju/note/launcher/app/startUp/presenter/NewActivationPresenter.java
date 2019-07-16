package com.muju.note.launcher.app.startUp.presenter;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.startUp.contract.NewActivationContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class NewActivationPresenter extends BasePresenter<NewActivationContract.View> implements NewActivationContract.Presenter {

    private static final String TAG = NewActivationPresenter.class.getSimpleName();

    /**
     * 查询平板是否激活
     *
     * @param iccid
     */
    @Override
    public void bindingDevice(String iccid) {
        OkGo.<String>post(UrlUtil.getQueryPadActiveState())
                .tag(this)
                .params("code", iccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            if (mView == null) {
                                LogUtil.e("mView为空");
                                return;
                            }
                            ActivePadInfo info = new Gson().fromJson(response.body(), ActivePadInfo.class);
                            if (info.getCode() != 200) {
                                mView.bindFail();
                                return;
                            }
                            ActivePadInfo.DataBean bean = info.getData().get(0);
                            if (bean.getActivetion() == 1) {
                                mView.bindSuccess(bean);
                            } else {
                                mView.bindFail();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.bindFail();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.bindFail();
                    }
                });

    }
}
