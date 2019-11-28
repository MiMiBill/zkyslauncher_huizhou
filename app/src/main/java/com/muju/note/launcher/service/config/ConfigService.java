package com.muju.note.launcher.service.config;


import android.content.Intent;
import android.security.keystore.KeyInfo;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.callkey.bean.CallKeyInfo;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.service.audio.PlayerAudioService;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.service.download.DownLoadService;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.AppUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
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

    public static int VIDEO_PAY_TIME=0;

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
                                LitePalDb.setZkysDb();
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
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(PadConfigDao.class, true).listen(new FindMultiCallback<PadConfigDao>() {
            @Override
            public void onFinish(List<PadConfigDao> list) {
                try {
                    LitePalDb.setZkysDb();
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

                            case "freeTime":
                                setVideoPayTime(dao);
                                break;
                            case "enableSwitchScreen":
                                CallKeyInfo.getsInstance().setEnableSwitchScreen(true);
                                LogUtil.d("使能了按键开关屏功能");
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

        boolean isLock = SystemUtils.isScreenOn();

        //在晚上模式的时候，才去判断需不需要重启平板
        if (type.equals(OPEN_PAD) && !CallKeyInfo.getsInstance().isDaytimeMode()) {
            CallKeyInfo.getsInstance().setDaytimeMode(true);
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
            CallKeyInfo.getsInstance().setDaytimeMode(false);
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
            File file=new File(SdcardConfig.RESOURCE_FOLDER,dao.getContent().hashCode()+".mp3");
            if(!file.exists()){
                DownLoadService.getInstance().downLoadHaseCode(dao.getContent(),".mp3");
            }
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
        File file=new File(SdcardConfig.RESOURCE_FOLDER,dao.getContent().hashCode()+".mp3");
        if(file.exists()){
            LogUtil.d(TAG,"音乐文件存在，直接播放:"+dao.getContent().hashCode()+".mp3");
            playAudio(dao);
            return;
        }else {
            LogUtil.d(TAG,"音乐文件不存在");
        }
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
        File file=new File(SdcardConfig.RESOURCE_FOLDER,path.hashCode()+".mp4");
        if(file.exists()){
            return;
        }
        DownLoadService.getInstance().downLoadHaseCode(path,".mp4");
    }


    /**
     *  设置影视免费时长
     * @param dao
     */
    private void setVideoPayTime(PadConfigDao dao){
        try {
            if(dao==null||dao.getPadConfigs()==null||dao.getPadConfigs().size()<=0){
                LogUtil.d(TAG,"影视时长配置为空");
                return;
            }
            PadConfigSubDao subDao=dao.getPadConfigs().get(0);
            if(subDao==null){
                return;
            }
            if(TextUtils.isEmpty(subDao.getContent())){
                return;
            }
            VIDEO_PAY_TIME=Integer.parseInt(subDao.getContent());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
