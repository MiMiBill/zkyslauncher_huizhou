package com.muju.note.launcher.app.hostipal.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.hostipal.bean.GetDownloadBean;
import com.muju.note.launcher.app.hostipal.contract.EncyContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class EncyclopsediasPresenter extends BasePresenter<EncyContract.View> implements EncyContract.Presenter {

    @Override
    public void getDownLoadUrl() {
        OkGo.<BaseBean<GetDownloadBean>>get(UrlUtil.getDb()).tag(this).execute(new JsonCallback<BaseBean<GetDownloadBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetDownloadBean>> response) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if (response.body().getData() instanceof GetDownloadBean) {
                    GetDownloadBean getDownloadBean = (GetDownloadBean) response.body().getData();
                    mView.getDownLoadUrl(getDownloadBean);
                }
            }

            @Override
            public void onError(Response<BaseBean<GetDownloadBean>> response) {
                super.onError(response);
//                LogFactory.l().i("onError" + response.body());
            }
        });
    }
}
