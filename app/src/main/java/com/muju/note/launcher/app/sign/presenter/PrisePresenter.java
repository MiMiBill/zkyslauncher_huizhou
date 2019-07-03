package com.muju.note.launcher.app.sign.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.app.sign.contract.PriseContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.user.UserUtil;

public class PrisePresenter extends BasePresenter<PriseContract.View> implements PriseContract.Presenter {

    @Override
    public void useReward(int typeId, int count) {
        OkGo.<BaseBean<PriseBean>>post(UrlUtil.useReward())
                .tag(this)
                .params("userId", UserUtil.getUserBean().getId())
                .params("typeId", typeId)
                .params("count", count)
                .execute(new JsonCallback<BaseBean<PriseBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<PriseBean>> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.useReward(response.body().getData());
                    }

                    @Override
                    public void onError(Response<BaseBean<PriseBean>> response) {
                        super.onError(response);
                    }
                });
    }
}
