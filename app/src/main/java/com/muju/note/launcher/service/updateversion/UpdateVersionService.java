package com.muju.note.launcher.service.updateversion;


import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.update.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 *  检查版本更新相关
 */
public class UpdateVersionService {

    private static final String TAG="UpdateVersionService";

    public static UpdateVersionService updateVersionService=null;
    public static UpdateVersionService getInstance(){
        if(updateVersionService==null){
            updateVersionService=new UpdateVersionService();
        }
        return updateVersionService;
    }

    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString();
    public static final String RESOURCE_FOLDER = SDCARD_FOLDER + "/zkys/resource/";

    public void start(){
        updateVersion();
    }

    /**
     * 检查更新
     */
    public void updateVersion() {
        ActivePadInfo.DataBean padActive = ActiveUtils.getPadActiveInfo();
        String imei = MobileInfoUtil.getIMEI(LauncherApplication.getInstance());
        int hospitalId = 0, deptId = 0;
        if (padActive != null) {
            hospitalId = padActive.getHospitalId();
            deptId = padActive.getDeptId();
        }
        Map<String, String> params = new HashMap();
        params.put("appType", "1");
        params.put("deviceType", "1");
        params.put("pkgName", "" + BuildConfig.APPLICATION_ID);
        params.put("versionCode", "" + BuildConfig.VERSION_CODE);
        params.put("hospitalId", "" + hospitalId);
        params.put("deptId", "" + deptId);
        params.put("code", imei);
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(LauncherApplication.getContext()));
        OkGo.<String>post(UrlUtil.getVersionCheckUpdate())
                .tag(this)
                .headers("SIGN", sign)
                .params(params)
                .cacheMode(CacheMode.NO_CACHE).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject obj = new JSONObject(body);
                    int code = obj.getInt("code");
                    if (code == 200) {
                        String url = obj.getJSONObject("data").getString("url");
                        downLoadApk(url, "launcher.apk");
                        //版本更新完成检查是否激活
                    } else if (code == -1) {
                        //询问服务器是否激活
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    /**
     *  下载APK
     * @param url
     * @param path
     */
    private void downLoadApk(String url,String path){
        if(TextUtils.isEmpty(url)||TextUtils.isEmpty(path)){
            LogUtil.e(TAG,"下载地址或路径有误，请检查，url:"+url+"    path:"+path);
            return;
        }
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback(SdcardConfig.RESOURCE_FOLDER, path) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        installApk(response.body().getName());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                    }
                });
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
                        String apkPath = apkFile.getAbsolutePath();
                        int resultCode = PackageUtils.installSilent(LauncherApplication.getContext(), apkPath);
                        if (resultCode != PackageUtils.INSTALL_SUCCEEDED) {
                            LogUtil.e(TAG,"升级失败" + apkFile.exists());
                        }
                    }
                });
    }

}
