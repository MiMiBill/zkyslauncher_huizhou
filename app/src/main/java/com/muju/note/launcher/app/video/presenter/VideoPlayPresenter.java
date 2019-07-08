package com.muju.note.launcher.app.video.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.video.contract.VideoPlayContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.url.UrlUtil;
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


    //创建订单
    @Override
    public void orderCreate(int comboId, String imei,int payType) {
        OkGo.<String>post(UrlUtil.orderCreate())
                .tag(UrlUtil.orderCreate())
                .params("comboId",comboId)
                .params("imei",imei)
                .params("payType",3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.orderCreate(response.body());
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
}
