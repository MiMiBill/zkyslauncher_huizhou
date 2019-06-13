package com.muju.note.launcher.app.publicAdress.presenter;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.publicAdress.contract.PublicContract;
import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogFactory;
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
                        LogFactory.l().i("onError");
                        if(listener!=null)
                            listener.verfyFail();
                    }
                });
    }


    @Override
    public void doTask(int userId,int advertId) {
        OkGo.<BaseBean<TaskBean>>post(UrlUtil.doTask())
                .tag(this)
                .params("userId", userId)
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                .params("advertId", advertId)
                .execute(new JsonCallback<BaseBean<TaskBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<TaskBean>> response) {
                        mView.doTask(response.body().getData());
                    }

                    @Override
                    public void onError(Response<BaseBean<TaskBean>> response) {
                        super.onError(response);
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
