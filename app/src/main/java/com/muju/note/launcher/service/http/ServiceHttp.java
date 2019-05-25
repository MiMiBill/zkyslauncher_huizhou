package com.muju.note.launcher.service.http;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.service.entity.PadCinfigNewEntity;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceHttp {

    public static ServiceHttp serviceHttp;

    public static ServiceHttp getInstance(){
        if(serviceHttp==null){
            serviceHttp=new ServiceHttp();
        }
        return serviceHttp;
    }

    /**
     * 获取平板配置信息
     *
     */
    public void getPadConfigs() {

        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        Map<String, String> params = new HashMap();
        params.put("hospitalId", ""+activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(LauncherApplication.getContext()));

        OkGo.<BaseBean<List<PadConfigDao>>>post(UrlUtil.getPadConfigsNew())
                .tag(this)
                .headers(Signature.PAD_SIGN,sign)
                .params(params)
                .execute(new JsonCallback<BaseBean<List<PadConfigDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<PadConfigDao>>> response) {

                        ExecutorService service=Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                PadConfigSubDao.deleteAll();
                                for (PadConfigDao dao:response.body().getData()){
                                    dao.save();
                                    for (PadConfigSubDao subDao:dao.getPadConfigs()){
                                        subDao.save();
                                    }
                                }
                            }
                        });

                    }
                });

    }

}
