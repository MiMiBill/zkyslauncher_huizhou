package com.muju.note.launcher.app.userinfo.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.userinfo.contract.FeedBackContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.sign.Signature;

import java.util.HashMap;
import java.util.Map;

import static com.muju.note.launcher.util.FormatUtils.FormatDateUtil.formatDate;

public class FeedBackPresenter extends BasePresenter<FeedBackContract.View> implements FeedBackContract.Presenter {

    @Override
    public void post(String content) {
        Map<String, String> params = new HashMap();
        params.put("did", MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        params.put("title", "");
        params.put("content", content);
        params.put("createTime", formatDate(formatDate));
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(LauncherApplication.getContext()));

        OkGo.<String>post(UrlUtil.getFeedbackContent())
                .headers(Constants.PAD_SIGN, sign)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                       mView.post(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mView.postFail();
                    }
                });
    }

}
