package com.muju.note.launcher.app.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.app.home.util.PatientUtil;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sign.Signature;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract
        .Presenter {
    private static final String TAG="HomePresenter";
    private Disposable diDateTimer;

    /**
     * 更新标题栏时间
     */
    public void updateDate() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        diDateTimer = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.getDate(DateUtil.getDate("yyyy年MM月dd日"), DateUtil.getDate
                                ("HH:mm:ss"), DateUtil.getWeek(), NetWorkUtil.getNetWorkLine(),
                                NetWorkUtil.getNetworkState(LauncherApplication.getContext()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPatientData(String padId, Context context) {
        Map<String, String> params = new HashMap();
        params.put("bedId", padId);
        params.put("disabled", "1");
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(context));
        OkGo.<String>post(UrlUtil.getGetPaitentInfo()).tag(this)
                .headers("SIGN", sign)
//                .params("bedId", padId)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
//                        LogFactory.l().i("response==="+response.body());
                        Gson gson = new Gson();
                        PatientResponse patientResponse = gson.fromJson(response.body(),
                                PatientResponse.class);
                        if (patientResponse.getCode() == 200 && patientResponse.getData()!=null) {
                            PatientResponse.DataBean patient = patientResponse.getData();
                            if (patient.getDisabled()) {
//                                SPUtil.saveDataList(Constants.PATIENT, patientResponse.getData());
                                PatientUtil.getInstance().setPatientData(patient);
                                mView.patientInfo(patient);
                            } else {
                                mView.notPatientInfo();
                            }

                        } else {
                            mView.notPatientInfo();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
//                        clearPatientData();
                        if (mView != null) {
                            mView.notPatientInfo();
                        }
                    }
                });
    }


    /**
     * 获取轮播广告
     */
    @Override
    public void getBananaList(String code) {
        LitePalDb.setZkysDb();
        LitePal.where("code =?",code).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
            @Override
            public void onFinish(List<AdvertsCodeDao> list) {
//                LogFactory.l().i("list"+list.size());
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getBananaNull();
                    return;
                }
                mView.getBananaList(list);
            }
        });
    }

    /**
     * 获取首页弹窗广告
     */
    @Override
    public void getDialogAd(String code) {
        LitePalDb.setZkysDb();
        LitePal.where("code =?",code).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
            @Override
            public void onFinish(List<AdvertsCodeDao> list) {
//                LogFactory.l().i("list"+list.size());
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    return;
                }
                mView.getDialogAd(list);
            }
        });
    }

    /**
     *  获取首页菜单模块
     */
    @Override
    public void getMenu() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(HomeMenuDao.class).listen(new FindMultiCallback<HomeMenuDao>() {
            @Override
            public void onFinish(List<HomeMenuDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getMenuNull();
                    return;
                }
                mView.getMenuSuccess(list);
            }
        });
    }

    /**
     * 获取播放记录
     */
    @Override
    public void getVideoHis() {
        LitePalDb.setZkysDb();
        LitePal.limit(20).order("createTime desc").findAsync(VideoHisDao.class).listen(new FindMultiCallback<VideoHisDao>() {
            @Override
            public void onFinish(List<VideoHisDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getVideoHisNull();
                    return;
                }
                mView.getVideoHisSuccess(list);
            }
        });
    }

    /**
     * 获取首页推荐影视
     */
    @Override
    public void getTopVideo() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(VideoInfoTopDao.class).listen(new FindMultiCallback<VideoInfoTopDao>
                () {
            @Override
            public void onFinish(List<VideoInfoTopDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getVideoTopNull();
                    return;
                }
                List<VideoInfoDao> topDaos = new ArrayList<>();
                for (VideoInfoTopDao dao : list) {
                    VideoInfoDao infoDao = new VideoInfoDao();
                    infoDao.setImgUrl(dao.getImgUrl());
                    infoDao.setColumnId(dao.getColumnId());
                    infoDao.setVideoType(dao.getVideoType());
                    infoDao.setScreenUrl(dao.getScreenUrl());
                    infoDao.setName(dao.getName());
                    infoDao.setDescription(dao.getDescription());
                    infoDao.setEditTime(dao.getEditTime());
                    infoDao.setCid(dao.getCid());
                    infoDao.setWatchCount(dao.getWatchCount());
                    topDaos.add(infoDao);
                }
                mView.getVideoTopImg(topDaos.get(0));
                topDaos.remove(0);
                mView.getVideoTopSuccess(topDaos);
            }
        });
    }

    public void onDestroy() {
        RxUtil.closeDisposable(diDateTimer);
    }

}
