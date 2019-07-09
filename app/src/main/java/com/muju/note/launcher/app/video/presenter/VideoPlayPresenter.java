package com.muju.note.launcher.app.video.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.video.contract.VideoPlayContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;

public class VideoPlayPresenter extends BasePresenter<VideoPlayContract.View> implements VideoPlayContract.Presenter {
    //查询套餐
    @Override
    public void getComboList(int hospitalId, int deptId) {
        OkGo.<String>post(UrlUtil.getComboList())
                .tag(UrlUtil.getComboList())
                .params("hospitalId",hospitalId)
                .params("deptId",deptId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.getComboList(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                    }
                });
    }

    //验证验证码
    @Override
    public void verfycode(String code) {
        OkGo.<String>post(UrlUtil.verCode())
                .params("code", code)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.verfycode(response.body());
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.verfycodeError();
                    }
                });
    }


    /**
     * 查询支付信息
     */
    public void setPayPackageList() {
        String imei = MobileInfoUtil.getIMEI(LauncherApplication.getInstance());
        OkGo.<String>post(UrlUtil.getGetDeviceStatus())
                .tag(this)
                .params("code", imei)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        String body = response.body();
                        mView.setPayPackageList(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                    }
                });
    }


    //轮播查询订单
    @Override
    public void intervalSLOrder() {
        String imei = MobileInfoUtil.getIMEI(LauncherApplication.getInstance());
        OkGo.<String>post(UrlUtil.getGetDeviceStatus())
                .tag(this)
                .params("code", imei)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        String body = response.body();
                        mView.intervalSLOrder(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                    }
                });
    }
}
