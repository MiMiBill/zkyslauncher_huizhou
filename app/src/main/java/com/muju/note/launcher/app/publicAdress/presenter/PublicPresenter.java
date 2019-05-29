package com.muju.note.launcher.app.publicAdress.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.publicAdress.contract.PublicContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.user.UserUtil;

public class PublicPresenter extends BasePresenter<PublicContract.View> implements PublicContract.Presenter {
    private PublicListener listener;
    @Override
    public void verfycode(String code,int adverId,String advertCode) {
        OkGo.<String>post(UrlUtil.verCode())
                .params("code", code)
                .params("userId", UserUtil.getUserBean().getId())
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                .params("advertId", adverId)
                .params("advertCode", advertCode)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(listener!=null)
                            listener.verfySuccess(response.body());
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if(listener!=null)
                            listener.verfyFail();
                    }
                });
    }





    public void setOnPublicListener(PublicListener listener){
        this.listener=listener;
    }

    public interface PublicListener{
        void verfySuccess(String data);
        void verfyFail();
    }
}
