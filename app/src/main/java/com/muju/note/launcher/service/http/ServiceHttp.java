package com.muju.note.launcher.service.http;

import android.annotation.SuppressLint;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.activeApp.entity.ResourceEntity;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.update.PackageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

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


    /**
     * 下载文件
     *
     * @param resourceEntity
     */
    public void downloadFile(final ResourceEntity resourceEntity) {
        File file = new File(SdcardConfig.RESOURCE_FOLDER, resourceEntity.getFileName());
        LogFactory.l().i("file.exists()=="+file.exists());
        if (file.exists() && resourceEntity.getType() != ResourceEntity.APP_UPDATE_PACKAGE) {
            shuntResource(resourceEntity);
            return;
        }
        OkGo.<File>get(resourceEntity.getUrl())
                .tag(this)
                .execute(new FileCallback(SdcardConfig.RESOURCE_FOLDER, resourceEntity.getFileName()) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        shuntResource(resourceEntity);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        LogFactory.l().i("progress:%s=="+progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        LogFactory.l().i("progress:%s=="+response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        LogUtil.i("progress:%s==onStart");
                    }
                });
    }

    private void shuntResource(ResourceEntity resourceEntity) {

        switch (resourceEntity.getType()) {
            case ResourceEntity.MISSION_PDF:
                EventBus.getDefault().post(resourceEntity);
                break;
            case ResourceEntity.MISSION_VIDEO:
                EventBus.getDefault().post(resourceEntity);
                break;
            case ResourceEntity.APP_UPDATE_PACKAGE:
                installApk(resourceEntity.getFileName());
                break;
           /* case ResourceEntity.TIMING_AUDIO:
                Intent intent = new Intent(LauncherApplication.getInstance(), PlayerAudioService.class);
                intent.putExtra("resource", resourceEntity.getFileName());
                LauncherApplication.getInstance().startService(intent);
                break;*/
            case ResourceEntity.ENCY_ZIP:
                EventBus.getDefault().post(resourceEntity);
                break;
            default:
                break;
        }
    }


    /**
     * 安装apk
     */
    @SuppressLint("CheckResult")
    private void installApk(final String fileName) {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        File apkFile = new File(SdcardConfig.RESOURCE_FOLDER, fileName);
                        LogUtil.d("file.exists()" + apkFile.exists());
                        String apkPath = apkFile.getAbsolutePath();
                        int resultCode = PackageUtils.installSilent(LauncherApplication.getInstance(), apkPath);
                        if (resultCode != PackageUtils.INSTALL_SUCCEEDED) {
                            LogUtil.d("升级失败" + apkFile.exists());
                        }
                    }
                });
    }

}
