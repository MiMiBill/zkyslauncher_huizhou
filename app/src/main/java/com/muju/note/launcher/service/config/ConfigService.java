package com.muju.note.launcher.service.config;


import android.content.Intent;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.service.audio.PlayerAudioService;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.service.http.ServiceHttp;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.AppUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.file.FileUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.system.SystemUtils;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 配置信息相关
 */
public class ConfigService {

    private static final String TAG = "ConfigService";

    public static ConfigService configService = null;

    public static ConfigService getInstance() {
        if (configService == null) {
            configService = new ConfigService();
        }
        return configService;
    }

    private static final String CLOSE_PAD = "closePad";
    private static final String OPEN_PAD = "openPad";

    public void start() {
        getPadConfigs();
    }

    /**
     * 获取平板配置信息
     */
    public void getPadConfigs() {
        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        Map<String, String> params = new HashMap();
        params.put("hospitalId", "" + activeInfo.getHospitalId());
        params.put("deptId", "" + activeInfo.getDeptId());
        OkGo.<BaseBean<List<PadConfigDao>>>post(UrlUtil.getPadConfigsNew())
                .tag(this)
                .params(params)
                .execute(new JsonCallback<BaseBean<List<PadConfigDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<PadConfigDao>>> response) {
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                PadConfigSubDao.deleteAll();
                                for (PadConfigDao dao : response.body().getData()) {
                                    dao.save();
                                    for (PadConfigSubDao subDao : dao.getPadConfigs()) {
                                        subDao.save();
                                    }
                                }
                            }
                        });

                    }
                });
    }

    /**
     * 执行配置文件操作
     */
    public void playConfig() throws Exception {
        LitePal.findAllAsync(PadConfigDao.class, true).listen(new FindMultiCallback<PadConfigDao>() {
            @Override
            public void onFinish(List<PadConfigDao> list) {
                try {
                    if (list == null || list.size() <= 0) {
                        LogUtil.e(TAG, "平板配置信息为空，请检查配置");
                        return;
                    }
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    LogUtil.d("当前时间：" + hour + ":" + minute);
                    for (PadConfigDao dao : list) {
                        switch (dao.getSort()) {
                            case "audio":
                                checkAudio(dao.getPadConfigs());
                                break;

                            case "openVideo":
                                downLoadOpneVideo(dao.getPadConfigs());
                                break;

                            case "launch":
                                lockScreen(dao.getPadConfigs());
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 执行开屏或关屏
     *
     * @param list
     * @throws Exception
     */
    private void lockScreen(List<PadConfigSubDao> list) throws Exception {
        String type = checkScreen(list);
        LogUtil.d(TAG, "开关屏类型：" + type);
        if (TextUtils.isEmpty(type)) {
            return;
        }
        boolean isLock = SystemUtils.isLock();
        if (type.equals(OPEN_PAD)) {
            if (isLock) {
                LogUtil.d(TAG, "当前处于开屏状态，无需在开屏");
            } else {
                LogUtil.d(TAG, "当前处于关屏状态，开启屏幕");
                Disposable di = Observable.timer(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                SystemUtils.turnOnScreen();
                                AppUtils.rebootPhone();
                            }
                        });


            }
        }
        if (type.equals(CLOSE_PAD)) {
            if (isLock) {
                LogUtil.d(TAG, "当前处于开屏状态，锁屏");
                Disposable di = Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                SystemUtils.screenOff();
                            }
                        });
            } else {
                LogUtil.d(TAG, "当前处于锁屏状态，无需在锁屏");
            }
        }
    }

    private String checkScreen(List<PadConfigSubDao> list) throws Exception {
        String nowDate = DateUtil.getNowHourAndMin();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                // 如果小于第一个item的时间，那么执行第一条的反操作
                if (!DateUtil.checkTime(nowDate, list.get(i).getActionTime())) {
                    if (TextUtils.equals(list.get(i).getType(), OPEN_PAD)) {
                        return CLOSE_PAD;
                    } else if (TextUtils.equals(list.get(i).getType(), CLOSE_PAD)) {
                        return OPEN_PAD;
                    }
                }
            }
            // 如果为最后一条item，那么直接执行当前操作
            if (i == list.size() - 1) {
                return list.get(i).getType();
            }

            // 如果当前时间大于item时间，并且小于下一item时间，执行当前item操作
            if (DateUtil.checkTime(nowDate, list.get(i).getActionTime()) && !DateUtil.checkTime(nowDate, list.get(i + 1).getActionTime())) {
                return list.get(i).getType();
            }
        }
        return null;
    }

    /**
     *  判断音乐文件是否需要播放
     */
    private void checkAudio(List<PadConfigSubDao> list){
        for (PadConfigSubDao dao:list){
            if(DateUtil.isTimeRight(dao.getActionTime(),DateUtil.getNowHourAndMin())){
                downLoadAudio(dao);
            }
        }
    }

    /**
     *  下载音乐文件并播放
     */
    private void downLoadAudio(final PadConfigSubDao dao){
        if(TextUtils.isEmpty(dao.getContent())){
            LogUtil.e(TAG,"音乐文件路径为空，请检查");

        }
        File file=new File(dao.getContent().hashCode()+".mp3");
        if(file.exists()){
            LogUtil.d(TAG,"音乐文件存在，直接播放:"+dao.getContent().hashCode()+".mp3");
            playAudio(dao);
            return;
        }
        OkGo.<File>get(dao.getContent())
                .tag(this)
                .execute(new FileCallback(SdcardConfig.RESOURCE_FOLDER, dao.getContent().hashCode()+".mp3") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        playAudio(dao);
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
     *  播放音频
     * @param dao
     */
    private void playAudio(PadConfigSubDao dao){
        Intent intent = new Intent(LauncherApplication.getContext(), PlayerAudioService.class);
        intent.putExtra("resource", dao.getContent().hashCode() + ".mp3");
        LauncherApplication.getContext().startService(intent);
    }

    /**
     *  下载每日播放视频
     */
    private void downLoadOpneVideo(List<PadConfigSubDao> daos){
        if(daos==null||daos.size()<=0){
            LogUtil.e(TAG,"每日播放视频为空，请检查");
            return;
        }
        String path=daos.get(0).getContent();
        File file=new File(path.hashCode()+".mp4");
        if(file.exists()){
            LogUtil.d(TAG,"每日视频已存在，无需下载");
            return;
        }
        OkGo.<File>get(path)
                .tag(this)
                .execute(new FileCallback(SdcardConfig.RESOURCE_FOLDER, path.hashCode()+".mp4") {
                    @Override
                    public void onSuccess(Response<File> response) {
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

}
