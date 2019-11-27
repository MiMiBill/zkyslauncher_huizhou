package com.muju.note.launcher.app.video.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.video.bean.WeiXinTask;
import com.muju.note.launcher.app.video.contract.VideoPlayContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
                        }else {
                            mView.getComboListFail();
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
                        mView.setPayFail();
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

    @Override
    public void getWeiXinTask(String hospitalId, String deptId) {



//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        RequestBody body = new FormBody.Builder()
//                .add("imei", imei)
//                .add("pushCode", "" + ActiveUtils.getPadActiveInfo().getBedId())
////                .add("format", "json")
//                .build();
//        Request request = new Request.Builder().url(UrlUtil.getWeiXinTasks(hospitalId,deptId))
//                .post(body)
//                .header("authorization","Bearer " + UserUtil.getUserBean().getAccessToken())
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d(e.getMessage());
//                if(mView==null){
//                    LogUtil.e("mView为空");
//                    return;
//                }else {
//                    mView.getWeiXinTaskFail();
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                if(mView==null){
//                    LogUtil.e("mView为空");
//                    return;
//                }
//                String body = response.body().string();
//                LogUtil.d("获取广告：" + body);
//                WeiXinTask weiXinTask = new Gson().fromJson(body, WeiXinTask.class);
//                if (weiXinTask != null && weiXinTask.isSuccessful())
//                {
//                    WeiXinTask.WeiXinTaskData data =  weiXinTask.getData();
//                    if (data != null)
//                    {
//                        mView.getWeiXinTask(data);
//                    }else {
//                        mView.getWeiXinTaskFail();
//                    }
//                }else {
//                    mView.getWeiXinTaskFail();
//                }
//            }
//        });

        String imei = MobileInfoUtil.getIMEI(LauncherApplication.getInstance());
        Map<String,String> map = new HashMap<>();
        map.put("imei",imei);
        map.put("pushCode","" + ActiveUtils.getPadActiveInfo().getBedId());
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,json);

        OkGo.<String>post(UrlUtil.getWeiXinTasks(hospitalId,deptId))
                .tag(this)
                .removeAllHeaders()
                .headers("authorization","Bearer " + UserUtil.getUserBean().getAccess_token())
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        String body = response.body();
                        WeiXinTask weiXinTask = new Gson().fromJson(body, WeiXinTask.class);
                        if (weiXinTask != null && weiXinTask.isSuccessful())
                        {
                            WeiXinTask.WeiXinTaskData data =  weiXinTask.getData();
                            if (data != null)
                            {
                                mView.getWeiXinTask(data);
                            }else {
                                mView.getWeiXinTaskFail();
                            }
                        }else {
                            mView.getWeiXinTaskFail();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }else {
                            mView.getWeiXinTaskFail();
                        }
                    }
                });

    }
}
