package com.muju.note.launcher.app.startUp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.startUp.contract.ActivationContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogFactory;

public class ActivationPresenter extends BasePresenter<ActivationContract.View> implements ActivationContract.Presenter {
    private ActivationListener listener;
    @Override
    public void bindingDevice(String iccid) {
        OkGo.<String>post(UrlUtil.getQueryPadActiveState())
                .tag(this)
                .params("code", iccid)
//                                .params("code", "padImei") //测试使用
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogFactory.l().i("mView==null");
                        }
                        if(listener!=null){
                            listener.bindSuccess(response.body());
                        }
//                        mView.bindSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //网络异常
//                        mView.bindFail();
                        if(listener!=null){
                            listener.bindFail();
                        }
                    }
                });
    }




    public void setOnActivationListener(ActivationListener listener){
        this.listener=listener;
    }

    public interface ActivationListener{
        void bindSuccess(String data);
        void bindFail();
    }
}
