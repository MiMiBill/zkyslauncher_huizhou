package com.muju.note.launcher.app.hostipal.service;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.SaveCallback;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * 医院风采相关
 */
public class MienService {

    public static MienService mienService = null;

    public static MienService getInstance() {
        if (mienService == null) {
            mienService = new MienService();
        }
        return mienService;
    }


    /**
     * 检查本地是否有医院风采数据
     */
    public void startMien() {
        LitePalDb.setZkysDb();
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_START));
        LitePal.countAsync(MienInfoDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if (count <= 0) {
                    getMienInfo();
                } else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_SUCCESS));
                }
            }
        });
    }

    /**
     * 获取医院风采信息并保存到数据库
     */
    public void getMienInfo() {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_HTTP_START));
        OkGo.<BaseBean<List<MienInfoDao>>>get(String.format(UrlUtil.getHospitalInfo(), ActiveUtils.getPadActiveInfo().getHospitalId()))
                .execute(new JsonCallback<BaseBean<List<MienInfoDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<MienInfoDao>>> response) {
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_DB_START));
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(MienInfoDao.class);
                                for (MienInfoDao dao : response.body().getData()) {
                                    dao.setMienId(dao.getId());
                                    dao.save();
                                }
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_SUCCESS));
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<MienInfoDao>>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_MIEN_HTTP_FAIL));
                    }
                });
    }


    private final  static int MAX_TRY_TIMES = 10;
    private int tryTimes = 0;
    /**
     * 更新失败后继续重试，共重试10次
     */
    public void getMienInfoTryTimes()
    {
        tryTimes ++;
        if (tryTimes > MAX_TRY_TIMES)  //最多重试5次
        {
            tryTimes = 0;
            return;
        }
        OkGo.<BaseBean<List<MienInfoDao>>>get(String.format(UrlUtil.getHospitalInfo(), ActiveUtils.getPadActiveInfo().getHospitalId()))
                .execute(new JsonCallback<BaseBean<List<MienInfoDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<MienInfoDao>>> response) {
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(MienInfoDao.class);

                                ArrayList<MienInfoDao> arrayList = new ArrayList<>();
                                for (MienInfoDao dao : response.body().getData()) {
                                    dao.setMienId(dao.getId());
                                    arrayList.add(dao);
                                }
                                LitePal.saveAllAsync(arrayList).listen(new SaveCallback() {
                                    @Override
                                    public void onFinish(boolean success) {
                                        if (success)
                                        {
                                            tryTimes = 0;
                                        }else {
                                            //保存失败那么3分钟后再去重试
                                            Observable.timer((long) (3), TimeUnit.MINUTES) //
                                                    .subscribe(new Consumer<Long>() {
                                                        @Override
                                                        public void accept(Long aLong) throws Exception {
                                                            LogUtil.d("getMienInfoTryTimes 3分钟定时器到了");
                                                            getMienInfoTryTimes();
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<MienInfoDao>>> response) {
                        super.onError(response);
                        //保存失败那么3分钟后再去重试
                        Observable.timer((long) (3), TimeUnit.MINUTES) //
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        LogUtil.d("getMienInfoTryTimes 3分钟定时器到了");
                                        getMienInfoTryTimes();
                                    }
                                });

                    }
                });
    }

}
