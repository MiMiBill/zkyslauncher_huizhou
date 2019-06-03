package com.muju.note.launcher.app.home.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.home.adapter.HomeHisVideoAdapter;
import com.muju.note.launcher.app.home.adapter.HomeTopVideoAdapter;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.event.PatientEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.presenter.HomePresenter;
import com.muju.note.launcher.app.hostipal.ui.EncyclopediasFragment;
import com.muju.note.launcher.app.hostipal.ui.HosPitalMissionFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.app.luckdraw.ui.LuckDrawFragment;
import com.muju.note.launcher.app.msg.ui.MsgFragment;
import com.muju.note.launcher.app.setting.ui.GuideFragment;
import com.muju.note.launcher.app.setting.ui.SettingFragment;
import com.muju.note.launcher.app.sign.ui.SignFragment;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.app.video.ui.VideoFragment;
import com.muju.note.launcher.app.video.ui.WoTvVideoLineFragment;
import com.muju.note.launcher.app.video.ui.WotvPlayFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.ClickTimeUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.util.file.FileUtils;
import com.muju.note.launcher.util.gilde.GlideUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.banana.Banner;
import com.unicom.common.VideoSdkConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View
        .OnClickListener {

    private static final String TAG = "HomeFragment";

    public static HomeFragment homeFragment = null;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.ll_hostipal)
    LinearLayout llHostipal;
    @BindView(R.id.ll_hostipal_ency)
    LinearLayout llHostipalEncy;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.ll_video_line)
    LinearLayout llVideoLine;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_hos_service)
    LinearLayout llHosService;
    @BindView(R.id.tv_bed)
    TextView tvBed;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_ency)
    TextView tvEncy;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_hos_card)
    TextView tvHosCard;
    @BindView(R.id.tv_hos_time)
    TextView tvHosTime;
    @BindView(R.id.tv_hos_doctor)
    TextView tvHosDoctor;
    @BindView(R.id.lly_have_paitent)
    LinearLayout llyHavePaitent;
    @BindView(R.id.ivPersonQrCode)
    ImageView ivPersonQrCode;
    @BindView(R.id.lly_no_patient)
    LinearLayout llyNoPatient;
    @BindView(R.id.ll_his_mission)
    LinearLayout llHisMission;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.rv_his_video)
    RecyclerView rvHisVideo;
    @BindView(R.id.ll_his_video_null)
    LinearLayout llHisVideoNull;
    @BindView(R.id.rv_video_top)
    RecyclerView rvVideoTop;
    @BindView(R.id.ll_top_video_null)
    LinearLayout llTopVideoNull;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.ll_msg)
    LinearLayout llMsg;
    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.lly_guide)
    LinearLayout llyGuide;
    @BindView(R.id.lly_sign)
    LinearLayout llySign;
    @BindView(R.id.lly_luck)
    LinearLayout llyLuck;
    @BindView(R.id.lly_cabinet)
    LinearLayout llyCabinet;
    Unbinder unbinder;

    private ActivePadInfo.DataBean activeInfo;
    private List<PatientResponse.DataBean> patientList = new ArrayList<>();

    private List<VideoHisDao> videoHisDaos;
    private HomeHisVideoAdapter homeHisVideoAdapter;

    private List<VideoInfoDao> videoInfoDaos;
    private HomeTopVideoAdapter homeTopVideoAdapter;

    private VideoInfoDao imgVideoInfo;
    private LoginDialog loginDialog;


    public static HomeFragment newInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        activeInfo = ActiveUtils.getPadActiveInfo();
        initBanner();
        saveRegisterId();
        patientList = SPUtil.getPatientList(Constants.PATIENT);
        if (patientList.size() > 0) {
            patientInfo(patientList.get(0));
        } else {
            if (activeInfo != null) {
                mPresenter.getPatientData(String.valueOf(activeInfo.getBedId()), getActivity());
            }
        }
        llHostipal.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llHostipalEncy.setOnClickListener(this);
        llVideoLine.setOnClickListener(this);
        llHisMission.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        llMsg.setOnClickListener(this);
        llyGuide.setOnClickListener(this);
        llySign.setOnClickListener(this);
        llyLuck.setOnClickListener(this);
        llyCabinet.setOnClickListener(this);
        llHosService.setOnClickListener(this);

        // 加载首页历史记录
        videoHisDaos = new ArrayList<>();
        homeHisVideoAdapter = new HomeHisVideoAdapter(R.layout.rv_item_home_his_video,
                videoHisDaos);
        rvHisVideo.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        rvHisVideo.setAdapter(homeHisVideoAdapter);
        mPresenter.getVideoHis();

        // 加载首页推荐影视
        videoInfoDaos = new ArrayList<>();
        homeTopVideoAdapter = new HomeTopVideoAdapter(R.layout.rv_home_video_top, videoInfoDaos);
        rvVideoTop.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 2,
                LinearLayoutManager.HORIZONTAL, false));
        rvVideoTop.setAdapter(homeTopVideoAdapter);
        mPresenter.getTopVideo();

        homeHisVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WotvPlayFragment wotvPlayFragment = new WotvPlayFragment();
                wotvPlayFragment.setHisDao(videoHisDaos.get(position));
                start(wotvPlayFragment);
            }
        });

        homeTopVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toPlay(videoInfoDaos.get(position));
            }
        });

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPlay(imgVideoInfo);
            }
        });
    }

    /**
     * 跳转播放
     *
     * @param infoDao
     */
    private void toPlay(VideoInfoDao infoDao) {
        if (!VideoSdkConfig.getInstance().getUser().isLogined()) {
            WoTvUtil.getInstance().login();
            showToast("登入视频中，请稍后");
            return;
        }
        VideoHisDao hisDao = new VideoHisDao();
        hisDao.setCid(infoDao.getCid());
        hisDao.setCustomTag(infoDao.getCustomTag());
        hisDao.setDescription(infoDao.getDescription());
        hisDao.setImgUrl(infoDao.getImgUrl());
        hisDao.setName(infoDao.getName());
        hisDao.setVideoId(infoDao.getVideoId());
        hisDao.setVideoType(infoDao.getVideoType());
        hisDao.setScreenUrl(infoDao.getScreenUrl());
        WotvPlayFragment wotvPlayFragment = new WotvPlayFragment();
        wotvPlayFragment.setHisDao(hisDao);
        start(wotvPlayFragment);
        llSetting.setOnClickListener(this);
    }


    //加载广告
    private void initBanner() {
        final AdvertsDialog dialog = new AdvertsDialog(getActivity(), R.style.dialog);
        try {
            NewAdvertsUtil.getInstance().queryAdverts(UIUtils.fun(AdvertsTopics.CODE_HOME_LB,
                    AdvertsTopics.CODE_HOME_DIALOG, AdvertsTopics.CODE_LOCK,
                    AdvertsTopics.CODE_PUBLIC, AdvertsTopics.CODE_VERTICAL,
                    AdvertsTopics.CODE_VIDEO_CORNER, AdvertsTopics.CODE_VIDEO_DIALOG,
                    AdvertsTopics.CODE_ROAD), banner, dialog);



            NewAdvertsUtil.getInstance().setOnBannerFailLisinter(new NewAdvertsUtil
                    .OnBannerFailLisinter() {
                @Override
                public void fail() {
                    NewAdvertsUtil.getInstance().showDefaultBanner(banner, 1);
                }
            });

            NewAdvertsUtil.getInstance().setOnBannerSuccessLisinter(new NewAdvertsUtil
                    .OnBannerSuccessLisinter() {
                @Override
                public void success() {
                    List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME_LB);
                    if (dataList.size() == 0) {
                        NewAdvertsUtil.getInstance().showDefaultBanner(banner, 1);
                    } else {
                        NewAdvertsUtil.getInstance().showByBanner(CacheUtil.getDataList
                                (AdvertsTopics.CODE_HOME_LB), banner);
                    }
                }
            });

            List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME_LB);
            if (dataList.size() == 0) {
                NewAdvertsUtil.getInstance().showDefaultBanner(banner, 1);
            }

            NewAdvertsUtil.getInstance().setOnDialogSuccessLisinter(new NewAdvertsUtil
                    .OnDialogSuccessLisinter() {
                @Override
                public void success() {
                    NewAdvertsUtil.getInstance().showByDialog(CacheUtil.getDataList(AdvertsTopics
                            .CODE_HOME_DIALOG), dialog);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new HomePresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPUtil.getBoolean("isSeekToPic")) {
            banner.setCurrentItem(0);
            banner.startAutoPlay();
            SPUtil.putBoolean("isSeekToPic", false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SPUtil.putBoolean("isSeekToPic", true);
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (banner!=null){
            banner.destroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.updateDate();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mPresenter.onDestroy();
    }

    /**
     * 设置状态栏时间
     *
     * @param date
     * @param time
     * @param week
     */
    @Override
    public void getDate(String date, String time, String week) {
        tvDate.setText(date);
        tvTime.setText(time);
        tvWeek.setText(week);
    }

    @Override
    public void patientInfo(PatientResponse.DataBean entity) {
        EventBus.getDefault().post(new PatientInfoEvent(entity));
        llyNoPatient.setVisibility(View.GONE);
        llyHavePaitent.setVisibility(View.VISIBLE);
        tvName.setText(entity.getUserName());
        tvAge.setText(entity.getAge() + "岁");
        tvSex.setText(entity.getSex() == 1 ? "男" : "女");
        tvHosDoctor.setText(entity.getChargeDoctor());
        tvHosTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity
                .getCreateTime())));
        tvBed.setText(activeInfo.getBedNumber() + "床");
        tvEncy.setText(activeInfo.getDeptName());
    }

    @Override
    public void notPatientInfo() {
        ivPersonQrCode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID
                (getContext()) + "," + JPushInterface.getRegistrationID(getContext()), 200, 200));
        llyNoPatient.setVisibility(View.VISIBLE);
        llyHavePaitent.setVisibility(View.GONE);
    }

    @Override
    public void getVideoHisSuccess(List<VideoHisDao> list) {
        videoHisDaos.addAll(list);
        homeHisVideoAdapter.notifyDataSetChanged();
    }


    @Override
    public void getVideoHisNull() {
        llHisVideoNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoTopSuccess(List<VideoInfoDao> list) {
        videoInfoDaos.addAll(list);
        homeTopVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void getVideoTopNull() {
        llTopVideoNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoTopImg(VideoInfoDao dao) {
        imgVideoInfo = dao;
        GlideUtil.loadImg(dao.getScreenUrl(), ivImg, R.mipmap.ic_video_load_default);
    }

    /**
     * 保存RegisterId到后台
     */
    @SuppressLint("CheckResult")
    private void saveRegisterId() {
        String regId = JPushInterface.getRegistrationID(getContext());
        LogUtil.d("jgId:%s", regId);
        if (TextUtils.isEmpty(regId)) {
            Observable.timer(1, TimeUnit.MINUTES)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            saveRegisterId();
                        }
                    });
            return;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PatientEvent event) {
        switch (event.getType()) {
            case PatientEvent.BIND:
                mPresenter.getPatientData(String.valueOf(activeInfo.getBedId()), getActivity());
                break;
            case PatientEvent.UN_BIND:
                FileUtils.deleteFile(Constants.FILE_VIP_GAME);
                FileUtils.deleteFile(Constants.FILE_VIP_VIDEO);
                // 通知wotv更新支付信息
                notPatientInfo();
                EventBus.getDefault().post(new PayEvent(PayEntity.ORDER_TYPE_VIDEO_PREMIUM));
                EventBus.getDefault().post(new VideoEvent(VideoEvent.PREMIUM));
                break;
            case PatientEvent.MISSION_ADD:
//                setHomeMissionData();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickTimeUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ll_hostipal:  // 医院风采
                start(new HospitalMienFragment());
                break;
            case R.id.ll_hostipal_ency:  // 医疗百科
                start(new EncyclopediasFragment());
                break;
            case R.id.ll_video:     // 视频
                start(VideoFragment.getIntance());
                break;
            case R.id.ll_hos_service:     //医疗服务
                showToast("更多精彩,敬请期待");
                break;
            case R.id.ll_video_line: // 直播TV
                start(new WoTvVideoLineFragment());
                break;

            case R.id.ll_his_mission: // 医院宣教
                start(new HosPitalMissionFragment());
                break;
            case R.id.ll_setting: // 设置
                start(new SettingFragment());
                break;
            case R.id.ll_msg: // 通知
                start(new MsgFragment());
                break;
            case R.id.lly_guide: // 新手引导
                start(new GuideFragment());
                break;
            case R.id.lly_sign: // 签到中心
                if(UserUtil.getUserBean()!=null){
                    start(new SignFragment());
                }else {
                    showLoginDialog(0);
                }
                break;
            case R.id.lly_luck: // 抽奖中心
                if(UserUtil.getUserBean()!=null){
                    start(new LuckDrawFragment());
                }else {
                    showLoginDialog(1);
                }
                break;
            case R.id.lly_cabinet: // 屏安柜
                showToast("更多精彩,敬请期待");
                break;
        }
    }


    private void showLoginDialog(final int type) {
        loginDialog = new LoginDialog(getActivity(), R.style.DialogFullscreen, new LoginDialog
                .OnLoginListener() {
            @Override
            public void onSuccess() {
                loginDialog.dismiss();
                if (type == 0) {
                    start(new SignFragment());
                } else {
                    start(new LuckDrawFragment());
                }
            }

            @Override
            public void onFail() {

            }
        });
        loginDialog.show();
    }
}

