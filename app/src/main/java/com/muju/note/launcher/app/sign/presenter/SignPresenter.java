package com.muju.note.launcher.app.sign.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.sign.contract.SignContract;
import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;

public class SignPresenter extends BasePresenter<SignContract.View> implements SignContract.Presenter {

    @Override
    public void checkSign(int id) {
        OkGo.<BaseBean<SignBean>>post(UrlUtil.checkSign())
                .tag(this)
                .params("userId", id)
                .cacheMode(CacheMode.NO_CACHE).execute(new JsonCallback<BaseBean<SignBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<SignBean>> response) {
                mView.checkSign(response.body().getData());
            }

            @Override
            public void onError(Response<BaseBean<SignBean>> response) {
                super.onError(response);
            }
        });
    }

    @Override
    public void checkSignStatus(int id) {
        OkGo.<BaseBean<SignStatusBean>>get(String.format(UrlUtil.checkSignStatus(), id))
                .tag(this)
                .execute(new JsonCallback<BaseBean<SignStatusBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<SignStatusBean>> response) {
                        mView.chesignStatus(response.body().getData());
                    }

                    @Override
                    public void onError(Response<BaseBean<SignStatusBean>> response) {
                        super.onError(response);
                    }
                });
    }
}
