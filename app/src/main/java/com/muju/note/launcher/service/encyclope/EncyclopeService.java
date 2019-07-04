package com.muju.note.launcher.service.encyclope;

import android.os.Environment;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.activeApp.entity.ResourceEntity;
import com.muju.note.launcher.app.hostipal.bean.EncyUpdateBean;
import com.muju.note.launcher.app.hostipal.bean.GetDownloadBean;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.listener.OnZipSuccessListener;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.zip.ZipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 医疗百科
 */
public class EncyclopeService {

    public static EncyclopeService encyService = null;
    private String dbName;
    private String outPathName = "medical.db";
    private String outPathString = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "zkysdb/" + outPathName;

    public static EncyclopeService getInstance() {
        if (encyService == null) {
            encyService = new EncyclopeService();
        }
        return encyService;
    }

    public void start() {
        EventBus.getDefault().register(this);
//        LitePal.deleteAll(InfoDao.class);
//        LitePal.deleteAll(InfomationDao.class);
        LitePal.countAsync(InfomationDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if (count <= 0) {
                    getDownLoadUrl();
                }
            }
        });
    }

    public void startEncy() {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_START));
        LitePal.countAsync(InfomationDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if (count <= 10000) {
                    getDownLoadUrl();
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_SUCCESS));
                }
            }
        });
    }


    public void getDownLoadUrl() {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_HTTP_START));
        OkGo.<BaseBean<GetDownloadBean>>get(UrlUtil.getDb())
                .tag(this)
                .execute(new JsonCallback<BaseBean<GetDownloadBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<GetDownloadBean>> response) {
                        downLoadZip(response.body());
                    }

                    @Override
                    public void onError(Response<BaseBean<GetDownloadBean>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_HTTP_FAIL));
                    }
                });
    }

    private void downLoadZip(final BaseBean<GetDownloadBean> bean) {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_DOWNLOAD_START));
        OkGo.<File>get(bean.getData().getPath())
                .execute(new FileCallback("/sdcard/zkys/resource/", "encyclope.zip") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        unZip(bean);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        int pro=(int)(progress.fraction*100);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_DOWNLOAD_PROGRESS,pro));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_DOWNLOAD_FAIL));
                    }
                });
    }

    private void unZip(final BaseBean<GetDownloadBean> bean) {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_UNZIP_START));
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                ZipUtils.UnZipFolder("/sdcard/zkys/resource/encyclope.zip", "/sdcard/zkysdb/encyclope.db", new OnZipSuccessListener() {
                    @Override
                    public void OnZipSuccess() {
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                insertInfoDb("/sdcard/zkysdb/encyclope.db", "medical_column",bean.getData().getCreateDate(),bean.getData().getCount());
//                                insertInfomationDb("/sdcard/zkysdb/encyclope.db", "medical_encyclopedia",bean.getData().getCreateDate(),bean.getData().getCount());
                            }
                        });
                    }
                });
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ResourceEntity entity) {
        if (entity.getType() == ResourceEntity.ENCY_ZIP) {
            String zipFileString = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "zkys/resource/" + dbName;
            ZipUtils.UnZipFolder(zipFileString, outPathString, new OnZipSuccessListener() {
                @Override
                public void OnZipSuccess() {
                    LitePal.countAsync(InfoDao.class).listen(new CountCallback() {
                        @Override
                        public void onFinish(int count) {
                            if (count <= 0) {
//                                insertInfoDb(outPathString, "medical_column");
                            }
                        }
                    });

                    LitePal.countAsync(InfomationDao.class).listen(new CountCallback() {
                        @Override
                        public void onFinish(int count) {
                            if (count <= 0) {
//                                insertInfomationDb(outPathString, "medical_encyclopedia");
                            }
                        }
                    });
                }
            });
        }
    }


    //插入病例列表
    private void insertInfomationDb(String path, String table,long createTime,int count) {
        try {
            DbHelper.insertEncyInfoMationDb(path, table,createTime,count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //插入科室列表
    private void insertInfoDb(String path, String table,long createTime,int count) {
        try {
            DbHelper.insertEncyInfoDb(path, table,createTime,count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取医院风采信息并保存到数据库
     */
    public void getLately() {
        HttpParams params = new HttpParams();
        params.put("timeStamp", SPUtil.getLong(Constants.SP_ENCY_UPDATE_TIME));
        OkGo.<String>post(UrlUtil.getLately())
                .params(params)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        LogFactory.l().e(response.body());
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                ExecutorService service = Executors.newSingleThreadExecutor();
                                String data = jsonObject.optString("data");
                                final EncyUpdateBean encyUpdateBean = gson.fromJson(data, EncyUpdateBean.class);

                                service.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            //更新科室
                                            if (encyUpdateBean.getColumns() != null && encyUpdateBean.getColumns().size() > 0) {
                                                LitePalDb.setZkysDb();
                                                for (InfoDao infoBean : encyUpdateBean.getColumns()) {
                                                    InfoDao dao = LitePal.where("columnId = ?", infoBean.getColumnId() + "").findFirst(InfoDao.class);
                                                    if (dao == null) {
                                                        dao.setColumnId(infoBean.getId());
                                                        dao.save();
                                                    } else {
                                                        LitePal.delete(InfoDao.class, dao.getColumnId());
                                                        dao.setColumnId(dao.getId());
                                                        dao.save();
                                                    }
                                                }
                                            }
                                            //更新第二张表
                                            if (encyUpdateBean.getMes() != null && encyUpdateBean.getMes().size() > 0) {
                                                LitePalDb.setZkysDb();
                                                for (InfomationDao infoBean : encyUpdateBean.getMes()) {
                                                    InfomationDao dao = LitePal.where("id = ?", infoBean.getColumnid() + "").findFirst(InfomationDao.class);
                                                    if (dao == null) {
                                                        dao.setId(infoBean.getId());
                                                        dao.save();
                                                    } else {
                                                        LitePal.delete(InfomationDao.class, dao.getId());
                                                        dao.setId(dao.getId());
                                                        dao.save();
                                                    }
                                                }
                                            }
                                            SPUtil.putLong(Constants.SP_ENCY_UPDATE_TIME, System.currentTimeMillis() / 1000);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
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

}
