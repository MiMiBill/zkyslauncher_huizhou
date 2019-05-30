package com.muju.note.launcher.app.luckdraw.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.luckdraw.contract.LuckContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogFactory;

public class LuckPresenter extends BasePresenter<LuckContract.View> implements LuckContract.Presenter {

    @Override
    public void startLuck(int id) {
        OkGo.<String>get(String.format(UrlUtil.startLuck(),id))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mView.startSuccess(response.body());
                        LogFactory.l().i(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.startFail();
                    }
                });
    }
}
