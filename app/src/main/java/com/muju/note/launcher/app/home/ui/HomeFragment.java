package com.muju.note.launcher.app.home.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.ui.CabinetFragment;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.bedsidecard.ui.BedSideCardFragment;
import com.muju.note.launcher.app.carerWorker.CarerWorkerFragment;
import com.muju.note.launcher.app.clide.ui.ClideFragment;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.finance.FinanceFragment;
import com.muju.note.launcher.app.game.ui.GameFragment;
import com.muju.note.launcher.app.healthy.ui.HealthyFragment;
import com.muju.note.launcher.app.home.adapter.HomeHisVideoAdapter;
import com.muju.note.launcher.app.home.adapter.HomeMenuAdapter;
import com.muju.note.launcher.app.home.adapter.HomeTopVideoAdapter;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.app.home.decoration.MyMarginDecoration;
import com.muju.note.launcher.app.home.dialog.HospitalServiceDialog;
import com.muju.note.launcher.app.home.event.DrawOutEvent;
import com.muju.note.launcher.app.home.event.GetAdvertEvent;
import com.muju.note.launcher.app.home.event.OutHospitalEvent;
import com.muju.note.launcher.app.home.event.PatientEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.presenter.HomePresenter;
import com.muju.note.launcher.app.hostipal.ui.HosPitalMissionFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalEncyFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalelServiceFragment;
import com.muju.note.launcher.app.insurance.InsureanceFragment;
import com.muju.note.launcher.app.msg.ui.MsgFragment;
import com.muju.note.launcher.app.orderfood.ui.OrderFoodFragment;
import com.muju.note.launcher.app.setting.ui.GuideFragment;
import com.muju.note.launcher.app.setting.ui.UserSettingFragment;
import com.muju.note.launcher.app.setting.ui.VoiceFragment;

import com.muju.note.launcher.app.shop.ShopFragment;
import com.muju.note.launcher.app.timetask.CrontabService;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.ui.VideoFragment;
import com.muju.note.launcher.app.video.ui.WoTvVideoLineFragment;
import com.muju.note.launcher.app.video.ui.WotvPlayFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.callkey.service.FileObserverService;
import com.muju.note.launcher.service.homemenu.HomeMenuService;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.ClickTimeUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.adverts.AdvertsUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.file.FileUtils;
import com.muju.note.launcher.util.gilde.GlideUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.banana.Banner;
import com.unicom.common.VideoSdkConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.ISupportFragment;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener {
    private static final String TAG = "HomeFragment";
    public static HomeFragment homeFragment = null;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_net)
    TextView tvNet;
    @BindView(R.id.tv_net_type)
    TextView tvNetType;
    @BindView(R.id.iv_net)
    ImageView ivNet;
    @BindView(R.id.iv_wifi)
    ImageView ivWifi;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.videoview)
    VideoView videoview;
//    @BindView(R.id.tv_bed_num)
//    TextView tvBedNum;
//    @BindView(R.id.tv_name)
//    TextView tvName;
//    @BindView(R.id.tv_sex)
//    TextView tvSex;
//    @BindView(R.id.tv_age)
//    TextView tvAge;
//    @BindView(R.id.tv_hos)
//    TextView tvHos;
//    @BindView(R.id.tv_dep)
//    TextView tvDep;
//    @BindView(R.id.tv_hos_doctor)
//    TextView tvHosDoctor;
//    @BindView(R.id.tv_hos_nurse)
//    TextView tvHosNurse;
//    @BindView(R.id.tv_hos_time)
//    TextView tvHosTime;
//    @BindView(R.id.tv_hl)
//    TextView tvHl;
//    @BindView(R.id.tv_food)
//    TextView tvFood;
//    @BindView(R.id.lly_have_paitent)
//    LinearLayout llyHavePaitent;
//    @BindView(R.id.tv_no_hos_info)
//    TextView tvNoHosInfo;
//    @BindView(R.id.ivPersonQrCode)
//    ImageView ivPersonQrCode;
//    @BindView(R.id.lly_no_patient)
//    LinearLayout llyNoPatient;
//    @BindView(R.id.rv_menu)
//    RecyclerView rvMenu;
    @BindView(R.id.rv_his_video)
    RecyclerView rvHisVideo;
    @BindView(R.id.ll_his_video_null)
    LinearLayout llHisVideoNull;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.rv_video_top)
    RecyclerView rvVideoTop;
//    @BindView(R.id.ll_top_video_null)
//    LinearLayout llTopVideoNull;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.rel_card)
    RelativeLayout relCard;

    @BindView(R.id.rl_banner_container)
    RelativeLayout rlBannerContainer;

//    @BindView(R.id.iv_bed_card)
//    ImageView ivBedCar
    private List<HomeMenuDao> homeMenuDaos;
    private HomeMenuAdapter menuAdapter;
    private ActivePadInfo.DataBean activeInfo;
    public static PatientResponse.DataBean entity;
    private List<VideoHisDao> videoHisDaos;
    private HomeHisVideoAdapter homeHisVideoAdapter;
    private List<VideoInfoDao> videoInfoDaos;
    private HomeTopVideoAdapter homeTopVideoAdapter;
    private VideoInfoDao imgVideoInfo;
    private AdvertsDialog dialog;
    private HospitalServiceDialog serviceDialog;
    private String netType = "";


    @BindView(R.id.btn_hospital_about)
    Button btnHospitalAbout;
    @BindView(R.id.btn_movie_about)
    Button btnMovieAbout;
    @BindView(R.id.btn_shopping_about)
    Button btnShoppingAbout;

    @BindView(R.id.rv_movie_menu)
    RecyclerView rvMovieMenu; //影视娱乐相关的菜单
    @BindView(R.id.hs_container)
    HorizontalScrollView hsContainer;
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.rl_hospital_mien_container)
    RelativeLayout rlHospitalMienContainer;
    @BindView(R.id.iv_hospital_mien_icon)
    ImageView ivHospitalMienIcon;
    @BindView(R.id.tv_hospital_mien_name)
    TextView tvHospitalMienName;
    @BindView(R.id.tv_middle_time)
    TextView tvMiddleTime;//中间的时间显示
    private boolean isHasHospitalMien =  false;//是否有医院风采模块


    private MyMarginDecoration myMarginDecoration;

    //影视娱乐相关的菜单
    private List<HomeMenuDao> rvMovieMenuDaos = new ArrayList<>();
    //医院相关的菜单
    private List<HomeMenuDao> rvHospitalMenuDaos = new ArrayList<>();
    //购物相关的菜单
    private List<HomeMenuDao> rvShoppingMenuDaos = new ArrayList<>();


    private int selectIndex = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeMessages(1);
                    int netDbm = (int) msg.obj;
                    setNetIcon(netType, netDbm);
                    break;
            }
        }
    };

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
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

        activeInfo = ActiveUtils.getPadActiveInfo();
        initBanner();
        saveRegisterId();
        if (activeInfo != null) {
            mPresenter.getPatientData(String.valueOf(activeInfo.getBedId()), getActivity());
        }
        relCard.setOnClickListener(this);
//        ivBedCard.setOnClickListener(this);

        // 加载首页菜单模块

//        menuAdapter = new HomeMenuAdapter(R.layout.rv_item_home_menu, homeMenuDaos);
//        rvMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 6));
//        rvMenu.setAdapter(menuAdapter);
//        rvMenu.setHasFixedSize(true);
//        rvMenu.setNestedScrollingEnabled(false);
//        homeMenuDaos = new ArrayList<>();
//        mPresenter.getMenu();
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
                LogFactory.l().i("当前音量=" + SystemUtils.getCurrentVolume(LauncherApplication
                        .getContext()));
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


        FileObserverService.start();
        //默认选择影视tab
        defaultSelect();
        initMovieModel();
        mPresenter.getMenu();

    }



    //初始化影视娱乐模块
    private void initMovieModel()
    {

        hsContainer.scrollTo(0,0);
        rlBannerContainer.setVisibility(View.VISIBLE);
        rlHospitalMienContainer.setVisibility(View.GONE);

        //设置菜单
        if (homeMenuDaos == null)
        {
            homeMenuDaos = new ArrayList<>();
            menuAdapter = new HomeMenuAdapter(R.layout.rv_item_home_menu, homeMenuDaos);
        }

        homeMenuDaos.clear();
        homeMenuDaos.addAll(rvMovieMenuDaos);
        int size = homeMenuDaos.size();
        if (size == 0)
        {
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 6));
        }else {
            int rowNum = (size % 2 == 0) ? ( size/2 ): (size/2 + 1);
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), rowNum));
        }


        rvMovieMenu.setAdapter(menuAdapter);
        rvMovieMenu.setHasFixedSize(true);
        rvMovieMenu.setNestedScrollingEnabled(false);

        if (myMarginDecoration != null)
        {
            rvMovieMenu.removeItemDecoration(myMarginDecoration);
        }
        myMarginDecoration = new MyMarginDecoration(getContext());
        rvMovieMenu.addItemDecoration(myMarginDecoration);

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                menuClick(homeMenuDaos.get(i));
            }
        });


        menuAdapter.notifyDataSetChanged();

    }

    //初始医疗相关模块
    private void initHospitalModel()
    {
        rlBannerContainer.setVisibility(View.GONE);
        hsContainer.scrollTo(0,0);

        if (homeMenuDaos == null)
        {
            homeMenuDaos = new ArrayList<>();
            menuAdapter = new HomeMenuAdapter(R.layout.rv_item_home_menu, homeMenuDaos);
        }
        homeMenuDaos.clear();
        homeMenuDaos.addAll(rvHospitalMenuDaos);
        int size = homeMenuDaos.size();

        if (size == 0)
        {
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 6));
        }else {
            int rowNum = (size % 2 == 0) ? ( size/2 ): (size/2 + 1);
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), rowNum));
        }

        rvMovieMenu.setAdapter(menuAdapter);
        rvMovieMenu.setHasFixedSize(true);
        rvMovieMenu.setNestedScrollingEnabled(false);

        //医院风采模块是否显示
        if (isHasHospitalMien)
        {
            rlHospitalMienContainer.setVisibility(View.VISIBLE);
        }else {
            rlHospitalMienContainer.setVisibility(View.GONE);
        }

        if (myMarginDecoration != null)
        {
            rvMovieMenu.removeItemDecoration(myMarginDecoration);
        }
        myMarginDecoration = new MyMarginDecoration(getContext());
        rvMovieMenu.addItemDecoration(myMarginDecoration);

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                menuClick(homeMenuDaos.get(i));
            }
        });


        menuAdapter.notifyDataSetChanged();

    }

    //初始点餐购物模块
    private void initShoppingModel()
    {
        rlBannerContainer.setVisibility(View.GONE);
        rlHospitalMienContainer.setVisibility(View.GONE);
        hsContainer.scrollTo(0,0);
        if (homeMenuDaos == null)
        {
            homeMenuDaos = new ArrayList<>();
            menuAdapter = new HomeMenuAdapter(R.layout.rv_item_home_menu, homeMenuDaos);
        }
        homeMenuDaos.clear();
        homeMenuDaos.addAll(rvShoppingMenuDaos);
        int size = homeMenuDaos.size();

        if (size == 0)
        {
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 6));
        }else {
            int rowNum = (size % 2 == 0) ? ( size/2 ): (size/2 + 1);
            rvMovieMenu.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), rowNum));
        }

        rvMovieMenu.setAdapter(menuAdapter);
        rvMovieMenu.setHasFixedSize(true);
        rvMovieMenu.setNestedScrollingEnabled(false);

        if (myMarginDecoration != null)
        {
            rvMovieMenu.removeItemDecoration(myMarginDecoration);
        }
        myMarginDecoration = new MyMarginDecoration(getContext());
        rvMovieMenu.addItemDecoration(myMarginDecoration);

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                menuClick(homeMenuDaos.get(i));
            }
        });


        menuAdapter.notifyDataSetChanged();

    }


    @OnClick({R.id.btn_shopping_about,
            R.id.btn_movie_about,
            R.id.btn_hospital_about
        })
    public void onTabSelect(Button btn)
    {
        clearStates();
        btn.getPaint().setFakeBoldText(true);
        btn.setBackgroundResource(R.mipmap.tab_select_bg);
        btn.setTextSize(30);

        switch (btn.getId())
        {
            case R.id.btn_movie_about:
            {
                selectIndex = 0;
                initMovieModel();

                break;
            }
            case R.id.btn_hospital_about:
            {
                selectIndex = 1;
                initHospitalModel();
                break;
            }
            case R.id.btn_shopping_about:
            {
                selectIndex = 2;
                initShoppingModel();
                break;
            }
        }
    }

    @OnClick({R.id.btn_setting,
            R.id.rl_hospital_mien_container
            })
    public void onModelClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_setting:
            {
                start(VoiceFragment.newInstance(1));
                break;
            }
            case R.id.rl_hospital_mien_container:
            {
                start(HospitalMienFragment.newInstance());
                break;
            }

        }
    }

    private void defaultSelect()
    {
        selectIndex = 0;
        btnMovieAbout.getPaint().setFakeBoldText(true);
        btnMovieAbout.setBackgroundResource(R.mipmap.tab_select_bg);
        btnMovieAbout.setTextSize(30);
    }

    private void clearStates()
    {
        setDefaultStates(btnHospitalAbout);
        setDefaultStates(btnMovieAbout);
        setDefaultStates(btnShoppingAbout);

    }

    private void setDefaultStates(Button button)
    {
        button.getPaint().setFakeBoldText(false);
        button.setTextSize(26);
        button.setBackgroundColor(Color.TRANSPARENT);
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
        start(wotvPlayFragment, ISupportFragment.SINGLETASK);
    }

    //加载广告
    private void initBanner() {
        dialog = new AdvertsDialog(getActivity(), R.style.dialog);
        try {
            AdvertsUtil.getInstance().queryAdverts(UIUtils.fun(AdvertsTopics.CODE_HOME_LB,
                    AdvertsTopics.CODE_HOME_DIALOG, AdvertsTopics.CODE_LOCK,
                    AdvertsTopics.CODE_PUBLIC, AdvertsTopics.CODE_VERTICAL,
                    AdvertsTopics.CODE_VIDEO_CORNER, AdvertsTopics.CODE_VIDEO_DIALOG,
                    AdvertsTopics.CODE_RW), banner, dialog);
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
        if (banner != null) {
            banner.destroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        hsContainer.scrollTo(0,0);
        mPresenter.updateDate();
        mPresenter.getTopVideo();
        mPresenter.getVideoHis();
        if (HomeMenuService.getInstance().isUpdate) {
            mPresenter.getMenu();
            HomeMenuService.getInstance().isUpdate = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mPresenter.onDestroy();
        handler.removeMessages(1);
        hideSoftInput();
        if (serviceDialog != null && serviceDialog.isShowing()) {
            serviceDialog.dismiss();
        }
    }

    /**
     * 设置状态栏时间
     *
     * @param date
     * @param time
     * @param week
     */
    @Override
    public void getDate(String date, String time, String week, String net, String netType) {
        tvDate.setText(date);
        tvTime.setText(time);
        if (!TextUtils.isEmpty(time))
        {
            String middleTime =  time.substring(0,time.lastIndexOf(":"));
            tvMiddleTime.setText(middleTime);
        }
        tvWeek.setText(week);
        tvNet.setText(net);
        if (netType.equals("WIFI")) {
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivNet.setVisibility(View.GONE);
            ivWifi.setVisibility(View.VISIBLE);
            int wifi = NetWorkUtil.getWifiLevel(LauncherApplication.getContext());
            if (wifi > -50 && wifi < 0) {//最强
                ivWifi.setImageResource(R.mipmap.wifi_level_good);
            } else if (wifi > -70 && wifi < -50) {//较强
                ivWifi.setImageResource(R.mipmap.wifi_level_better);
            } else if (wifi > -80 && wifi < -70) {//较弱
                ivWifi.setImageResource(R.mipmap.wifi_level_normal);
            } else if (wifi > -100 && wifi < -80) {//微弱
                ivWifi.setImageResource(R.mipmap.wifi_level_bad);
            } else {
                ivWifi.setImageResource(R.mipmap.wifi_level_none);
            }
        } else if (netType.equals("无网络连接") || netType.equals("未知")) {
            ivWifi.setVisibility(View.VISIBLE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivWifi.setImageResource(R.mipmap.wifi_level_none);
            ivNet.setImageResource(R.mipmap.net_level_none);
        } else {
            this.netType = netType;
            ivWifi.setVisibility(View.GONE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int netDbm = NetWorkUtil.getCurrentNetDBM(LauncherApplication.getContext());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = netDbm;
                    handler.sendMessage(message);
                }
            }).run();
        }
    }

    private void setNetIcon(String netType, int netDbm) {
        if (netType.equals("4G")) {
            if (netDbm > -95) {
                ivNet.setImageResource(R.mipmap.net_level_good);
            } else if (netDbm > -110) {
                ivNet.setImageResource(R.mipmap.net_level_better);
            } else if (netDbm > -125) {
                ivNet.setImageResource(R.mipmap.net_level_normal);
            } else if (netDbm > -140) {
                ivNet.setImageResource(R.mipmap.net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.net_level_none);
            }
        } else {
            if (netDbm > -75) {
                ivNet.setImageResource(R.mipmap.net_level_good);
            } else if (netDbm > -90) {
                ivNet.setImageResource(R.mipmap.net_level_better);
            } else if (netDbm > -105) {
                ivNet.setImageResource(R.mipmap.net_level_normal);
            } else if (netDbm > -120) {
                ivNet.setImageResource(R.mipmap.net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.net_level_none);
            }
        }
    }

    //设置病人信息
    @Override
    public void patientInfo(PatientResponse.DataBean entity) {
        LogFactory.l().i("设置病人信息");
        this.entity = entity;
        EventBus.getDefault().post(new PatientInfoEvent(entity));
//        llyNoPatient.setVisibility(View.GONE);
//        llyHavePaitent.setVisibility(View.VISIBLE);
//        tvName.setText(entity.getUserName());
//        tvAge.setText(entity.getAge() + "岁");
//        tvSex.setText(entity.getSex() == 1 ? "男" : "女");
//        tvHosDoctor.setText(entity.getChargeDoctor());
//        tvHosTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity.getCreateTime())));
//        tvBedNum.setText(activeInfo.getBedNumber());
//        tvHos.setText(activeInfo.getHospitalName());
//        tvFood.setText(entity.getDietCategory());
//        tvHl.setText(entity.getNursingLevel());
//        tvDep.setText(activeInfo.getDeptName());
//        tvHosNurse.setText(entity.getChargeNurse());
        //获取定时任务列表
        CrontabService.getInstance().start();
    }

    //没有入院信息
    @Override
    public void notPatientInfo() {
        this.entity = null;
        EventBus.getDefault().post(new OutHospitalEvent());
//        tvNoHosInfo.setText(activeInfo.getHospitalName() + "-" + activeInfo.getDeptName() + "-" +
//                activeInfo.getBedNumber() + "床");
//        ivPersonQrCode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID
//                (getContext()) + "," + JPushInterface.getRegistrationID(getContext()), 200, 200));
//        llyNoPatient.setVisibility(View.VISIBLE);
//        llyHavePaitent.setVisibility(View.GONE);
    }

    @Override
    public void getVideoHisSuccess(List<VideoHisDao> list) {
        llHisVideoNull.setVisibility(View.GONE);
        videoHisDaos.clear();
        videoHisDaos.addAll(list);
        homeHisVideoAdapter.notifyDataSetChanged();
    }


    @Override
    public void getVideoHisNull() {
        llHisVideoNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoTopSuccess(List<VideoInfoDao> list) {
//        llTopVideoNull.setVisibility(View.GONE);
        videoInfoDaos.clear();
        videoInfoDaos.addAll(list);
        homeTopVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void getVideoTopNull() {
//        llTopVideoNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoTopImg(VideoInfoDao dao) {
        imgVideoInfo = dao;
        GlideUtil.loadImg(dao.getScreenUrl(), ivImg, R.mipmap.ic_video_load_default);
    }

    @Override
    public void getBananaNull() {
        AdvertsUtil.getInstance().showDefaultBanner(banner, 1);
    }

    @Override
    public void getBananaList(List<AdvertsCodeDao> list) {
        AdvertsUtil.getInstance().showByDbBanner(list, banner);
    }

    @Override
    public void getDialogAd(List<AdvertsCodeDao> list) {
        AdvertsUtil.getInstance().showByDialog(list, dialog);
    }

    @Override
    public void getMenuSuccess(List<HomeMenuDao> list) {

//         "风采":
//         "设置":
        btnSetting.setVisibility(View.GONE);
        isHasHospitalMien = false;

        for (HomeMenuDao homeMenuDao : list)
        {
            if (
                    homeMenuDao.getTab().equalsIgnoreCase("影视") ||
                    homeMenuDao.getTab().equalsIgnoreCase("直播") ||
                    homeMenuDao.getTab().equalsIgnoreCase("游戏") ||
                    homeMenuDao.getTab().equalsIgnoreCase("儿童")
            ){
                rvMovieMenuDaos.add(homeMenuDao);
            }

            if (    homeMenuDao.getTab().equalsIgnoreCase("宣教") ||
                    homeMenuDao.getTab().equalsIgnoreCase("百科") ||
                    homeMenuDao.getTab().equalsIgnoreCase("健康") ||
                    homeMenuDao.getTab().equalsIgnoreCase("医疗") ||
                    homeMenuDao.getTab().equalsIgnoreCase("金融") ||
                    homeMenuDao.getTab().equalsIgnoreCase("保险") ||
                    homeMenuDao.getTab().equalsIgnoreCase("护工")
            ){
                rvHospitalMenuDaos.add(homeMenuDao);
            }

            if (
                    homeMenuDao.getTab().equalsIgnoreCase("购物") ||
                    homeMenuDao.getTab().equalsIgnoreCase("柜子") ||
                    homeMenuDao.getTab().equalsIgnoreCase("新手") ||
                    homeMenuDao.getTab().equalsIgnoreCase("用户") ||
                    homeMenuDao.getTab().equalsIgnoreCase("通知") ||
                    homeMenuDao.getTab().equalsIgnoreCase("点餐")
            ){
                rvShoppingMenuDaos.add(homeMenuDao);
            }

            if (homeMenuDao.getTab().equalsIgnoreCase("设置"))
            {
                btnSetting.setVisibility(View.VISIBLE);
            }

            if (homeMenuDao.getTab().equalsIgnoreCase("风采"))
            {
                isHasHospitalMien = true;
                if (TextUtils.isEmpty(homeMenuDao.getIcon())) {
                    ivHospitalMienIcon.setImageResource(R.mipmap.ic_home_item_hositpal);
                } else {
                    GlideUtil.loadImg(homeMenuDao.getIcon(), ivHospitalMienIcon, R.mipmap.ic_video_load_default);
                }
                if (TextUtils.isEmpty(homeMenuDao.getName())) {
                    tvHospitalMienName.setText("医院风采");
                } else {
                    tvHospitalMienName.setText(homeMenuDao.getName());
                }
            }


        }
        initMovieModel();


    }

    @Override
    public void getMenuNull() {
        showToast("首页模块配置为空，请联系管理人员检查");
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

    //获取广告
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetAdvertEvent event) {
        mPresenter.getBananaList(AdvertsTopics.CODE_HOME_LB);
        mPresenter.getDialogAd(AdvertsTopics.CODE_HOME_DIALOG);
    }


    @Override
    public void onClick(View v) {
        if (ClickTimeUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.rel_card: // 侧边栏
                EventBus.getDefault().post(new DrawOutEvent());
                break;
//            case R.id.iv_bed_card:
//                start(BedSideCardFragment.newInstance(HomeFragment.entity, false));
//                break;
        }
    }

    private void menuClick(HomeMenuDao dao) {
        switch (dao.getTab()) {
            case "风采":
                start(HospitalMienFragment.newInstance());
                break;
            case "宣教":
                start(new HosPitalMissionFragment());
                break;
            case "百科":
                start(new HospitalEncyFragment());
                break;
            case "柜子":
                start(new CabinetFragment());
                break;
            case "影视":
                start(new VideoFragment());
                break;
            case "用户":
                start(new UserSettingFragment());
                break;
            case "游戏":
                start(new GameFragment());
                break;
            case "直播":
                start(new WoTvVideoLineFragment(), ISupportFragment.SINGLETASK);
                break;
            case "购物":
                start(new ShopFragment());
                break;
            case "金融":
                start(new FinanceFragment());
                break;
            case "保险":
                start(new InsureanceFragment());
                break;
            case "点餐":
                start(new OrderFoodFragment());
                break;
            case "通知":
                start(new MsgFragment());
                break;
            case "新手":
                start(GuideFragment.newInstance(1));
                break;
            case "设置":
                start(VoiceFragment.newInstance(1));
                break;
            case "儿童":
                start(new ClideFragment());
                break;
            case "健康":
                start(new HealthyFragment());
                break;
            case "医疗":
                showServiceDialog();
                break;
            case "护工":
                start(new CarerWorkerFragment());
                break;
        }
    }


    /**
     *  医疗服务登录弹窗
     */
    private void showServiceDialog() {
        serviceDialog=new HospitalServiceDialog(getActivity(), R.style.DialogFullscreen, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_login){
                    if (TextUtils.isEmpty(serviceDialog.getEtNum())) {
                        showToast("请输入住院号");
                        return;
                    }
                    if (serviceDialog.getEtNum().equals("456789123")) {
                        serviceDialog.dismiss();
                        start(new HospitalelServiceFragment());
                    } else {
                        showToast("住院号不正确，请重新输入");
                    }
                }
            }
        });
        serviceDialog.show();
    }


}

