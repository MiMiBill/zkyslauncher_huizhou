package com.muju.note.launcher.service.uploaddata;

import android.os.Environment;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.db.AdvertsCountDao;
import com.muju.note.launcher.app.home.db.AdvertsInfoDao;
import com.muju.note.launcher.app.home.db.ModelCountDao;
import com.muju.note.launcher.app.home.db.ModelInfoDao;
import com.muju.note.launcher.app.hostipal.db.MissionCountDao;
import com.muju.note.launcher.app.video.db.VideoPlayerCountDao;
import com.muju.note.launcher.app.video.db.VideoPlayerInfoDao;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.litepal.UpAdvertInfoDao;
import com.muju.note.launcher.litepal.UpVideoInfoDao;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据上传相关
 */
public class UpLoadDataService {

    private static final String TAG = "UpLoadDataService";

    public static UpLoadDataService upLoadService = null;

    public static UpLoadDataService getInstance() {
        if (upLoadService == null) {
            upLoadService = new UpLoadDataService();
        }
        return upLoadService;
    }

    private boolean isUpLoad = false;

    public void start() {
        int videoDay = SPUtil.getInt(SpTopics.SP_UPLOAD_DATA_DAY);
        Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        if (day != videoDay) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour < 7) {
                if (isUpLoad) {
                    return;
                }
                isUpLoad = true;
                // 一个小时内随机获取
                int num = new Random().nextInt(59) + 1;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 上传数据信息
                        upLoad();
                        SPUtil.putInt(SpTopics.SP_UPLOAD_DATA_DAY, day);
                    }
                }, 1000 * 60 * num);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 以防万一，12个小时候重置状态
                        isUpLoad = false;
                    }
                }, 1000 * 60 * 60 * 12);
            }
        }
    }

    private void upLoad() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 上传广告统计总数信息
                    upAdvertCountDb();
                    // 上传影视统计总数信息
                    upVideoCountDb();
                    // 上传模块统计总数信息
                    upModelCountDb();
                    // 复制数据到data数据库
//                moveDbToData();
                    // 获取token，上传数据库到七牛云
                    getToken(Constants.ZKYS_PAD_DB);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 上传广告总数信息
     */
    public void upAdvertCountDb() {
        LitePalDb.setZkysDb();
        List<AdvertsCountDao> daoList = LitePal.findAll(AdvertsCountDao.class);
        OkGo.<BaseBean<Void>>post(UrlUtil.getUpCountDb())
                .params("data", new Gson().toJson(daoList))
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {
                        LitePalDb.setZkysDb();
                        LitePal.deleteAll(AdvertsCountDao.class);
                    }
                });
    }


    /**
     * 上传影视数据
     */
    public void upVideoCountDb() {
        LitePalDb.setZkysDb();
        List<VideoPlayerCountDao> daoList = LitePal.findAll(VideoPlayerCountDao.class);
        OkGo.<BaseBean<Void>>post(UrlUtil.getUpVideoCountDb())
                .params("data", new Gson().toJson(daoList))
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {
                        LitePalDb.setZkysDb();
                        LitePal.deleteAll(VideoPlayerCountDao.class);
                    }
                });
    }

    /**
     *  上传模块统计数据
     */
    public void upModelCountDb(){
        LitePalDb.setZkysDb();
        List<ModelCountDao> daoList=LitePal.findAll(ModelCountDao.class);
        OkGo.<BaseBean<Void>>post(UrlUtil.getUpModelCountDb())
                .params("data", new Gson().toJson(daoList))
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {
                        LitePalDb.setZkysDb();
                        LitePal.deleteAll(ModelCountDao.class);
                    }
                });
    }

    /**
     *  上传
     */
    public void upLoadMissionCountDb(){
        LitePalDb.setZkysDb();
        List<MissionCountDao> daoList=LitePal.findAll(MissionCountDao.class);
        OkGo.<BaseBean<Void>>post(UrlUtil.getUpMissionCountDb())
                .params("data", new Gson().toJson(daoList))
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {
                        LitePalDb.setZkysDb();
                        LitePal.deleteAll(ModelCountDao.class);
                    }
                });
    }

    /**
     * 移动数据到data数据库
     */
    private void moveDbToData() {
        // 移动广告详情数据到data数据库
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(AdvertsInfoDao.class).listen(new FindMultiCallback<AdvertsInfoDao>() {
            @Override
            public void onFinish(List<AdvertsInfoDao> list) {
                LitePalDb.setZkysDataDb();
                for (AdvertsInfoDao dao : list) {
                    UpAdvertInfoDao infoDao = new UpAdvertInfoDao();
                    infoDao.setAdvertId(dao.getAdvertId());
                    infoDao.setDate(dao.getDate());
                    infoDao.setDepId(dao.getDepId());
                    infoDao.setEndTime(dao.getEndTime());
                    infoDao.setHosId(dao.getHosId());
                    infoDao.setImei(dao.getImei());
                    infoDao.setStartTime(dao.getStartTime());
                    infoDao.setTime(dao.getTime());
                    infoDao.setType(dao.getType());
                    infoDao.save();
                }
                LitePalDb.setZkysDb();
                LitePal.deleteAll(AdvertsInfoDao.class);
            }
        });

        // 移动影视详情数据到data数据库
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(VideoPlayerInfoDao.class).listen(new FindMultiCallback<VideoPlayerInfoDao>() {
            @Override
            public void onFinish(List<VideoPlayerInfoDao> list) {
                LitePalDb.setZkysDataDb();
                for (VideoPlayerInfoDao dao : list) {
                    UpVideoInfoDao infoDao = new UpVideoInfoDao();
                    infoDao.setDate(dao.getDate());
                    infoDao.setDepId(dao.getDepId());
                    infoDao.setEndTime(dao.getEndTime());
                    infoDao.setHosId(dao.getHosId());
                    infoDao.setImei(dao.getImei());
                    infoDao.setStartTime(dao.getStartTime());
                    infoDao.setVideoId(dao.getVideoId());
                    infoDao.setVideoName(dao.getVideoName());
                    infoDao.save();
                }
                LitePalDb.setZkysDb();
                LitePal.deleteAll(VideoPlayerInfoDao.class);
            }
        });
    }

    /**
     * 获取七牛云tokend
     *
     * @param bucketName
     */
    public void getToken(String bucketName) {
        OkGo.<String>get(String.format(UrlUtil.getQnToken(), bucketName))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                String token = jsonObject.optString("token");
                                boolean isMount = Environment.getExternalStorageState().equals
                                        (android.os.Environment.MEDIA_MOUNTED);
                                if (isMount) {
                                    String path = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + File.separator +
                                            "zkysdb/zkys-data.db";
                                    File file = new File(path);
                                    if (file.exists()) {
                                        LogUtil.d(TAG, "数据库文件" + file.exists());
                                    }
                                    String key = MobileInfoUtil.getIMEI(LauncherApplication
                                            .getContext()) + "_" + getStringDate();
                                    upLoadQN(file, key, token);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String getStringDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date d = new Date(System.currentTimeMillis());
        return format.format(d);
    }

    /**
     * 上传数据到七牛云
     *
     * @param data
     * @param key
     * @param token
     */
    public void upLoadQN(File data, String key, String token) {
        Configuration config = new Configuration.Builder().build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            try {
                                LogUtil.d(TAG, "上传七牛云成功");
//                            LitePalDb.setZkysDataDb();
//                            LitePal.deleteAll(UpVideoInfoDao.class);
//                            LitePal.deleteAll(UpAdvertInfoDao.class);
                                DbHelper.clearTable(LitePalDb.DBNAME_ZKYS_DATA, UpAdvertInfoDao
                                        .class.getSimpleName());
                                DbHelper.clearTable(LitePalDb.DBNAME_ZKYS_DATA, UpVideoInfoDao
                                        .class.getSimpleName());
                                DbHelper.clearTable(LitePalDb.DBNAME_ZKYS_DATA, ModelInfoDao
                                        .class.getSimpleName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            LogUtil.e(TAG, "上传七牛云失败");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                    }
                }, null);
    }


}
