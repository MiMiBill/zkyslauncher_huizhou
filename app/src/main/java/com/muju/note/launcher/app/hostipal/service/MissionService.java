package com.muju.note.launcher.app.hostipal.service;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.service.download.DownLoadService;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindMultiCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MissionService {

    private static final String TAG = "MissionService";

    public static MissionService missionService = null;

    public static MissionService getInstance() {
        if (missionService == null) {
            missionService = new MissionService();
        }
        return missionService;
    }

    public void startMiss() {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_START));
        LitePalDb.setZkysDb();
        LitePal.countAsync(MissionInfoDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if (count <= 10) {
                    updateMission(1);
                } else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_SUCCESS));
                }
            }
        });
    }

    /**
     * 更新宣教信息
     */
    public void updateMission(final int status) {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_HTTP_START));
        Map<String, String> params = new HashMap();
        params.put("hospitalId", "" + ActiveUtils.getPadActiveInfo().getHospitalId());
        params.put("deptId", "" + ActiveUtils.getPadActiveInfo().getDeptId());
        OkGo.<BaseBean<List<MissionInfoDao>>>post(UrlUtil.getHospitalMission())
                .params(params)
                .execute(new JsonCallback<BaseBean<List<MissionInfoDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<MissionInfoDao>>> response) {
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_DB_START));
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(MissionInfoDao.class);
                                for (MissionInfoDao dao : response.body().getData()) {
                                    dao.setMissionId(dao.getId());
                                    dao.save();
                                }
                                if (status == 1) {
                                    downMission();
                                }
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_SUCCESS));
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<MissionInfoDao>>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MISS_HTTP_FAIL));
                    }
                });

    }

    /**
     * 下载宣教视频
     */
    public void downMission() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(MissionInfoDao.class).listen(new FindMultiCallback<MissionInfoDao>() {
            @Override
            public void onFinish(List<MissionInfoDao> list) {
                for (MissionInfoDao dao : list) {
                    if (TextUtils.isEmpty(dao.getVideo())) {
                        downLoad(dao.getFrontCover(), 1);
                    } else {
                        downLoad(dao.getVideo(), 2);
                    }
                }
            }
        });
    }

    public void downLoad(String url, int tag) {
        File file;
        if (url != null) {
            if (tag == 1) {
                file = new File(SdcardConfig.RESOURCE_FOLDER, url.hashCode() + ".pdf");
            } else {
                file = new File(SdcardConfig.RESOURCE_FOLDER, url.hashCode() + ".mp4");
            }
            if (file.exists()) {
                LogUtil.d(TAG, "文件已下载，无需重新下载:" + file.getAbsolutePath());
                return;
            }
            if (tag == 1) {
                DownLoadService.getInstance().downLoadHaseCode(url, ".pdf");
            } else {
                DownLoadService.getInstance().downLoadHaseCode(url, ".mp4");
            }
        }
    }

}
