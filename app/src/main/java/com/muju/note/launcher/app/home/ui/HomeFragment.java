package com.muju.note.launcher.app.home.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.event.PatientEvent;
import com.muju.note.launcher.app.home.presenter.HomePresenter;
import com.muju.note.launcher.app.hostipal.ui.EncyclopediasFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.ui.VideoFragment;
import com.muju.note.launcher.app.video.ui.WoTvVideoLineFragment;
import com.muju.note.launcher.base.BaseFragment;
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
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.banana.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    Unbinder unbinder;
    @BindView(R.id.lly_have_paitent)
    LinearLayout llyHavePaitent;
    @BindView(R.id.ivPersonQrCode)
    ImageView ivPersonQrCode;
    @BindView(R.id.lly_no_patient)
    LinearLayout llyNoPatient;
    Unbinder unbinder1;
    private ActivePadInfo.DataBean activeInfo;
    private List<PatientResponse.DataBean> patientList = new ArrayList<>();

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
    }


    //加载广告
    private void initBanner() {
        final AdvertsDialog dialog = new AdvertsDialog(getActivity(), R.style.dialog);
        try {
            NewAdvertsUtil.getInstance().queryAdverts(UIUtils.fun(AdvertsTopics.CODE_HOME,
                    AdvertsTopics.CODE_HOME_DIALOG, AdvertsTopics.CODE_LOCK,
                    AdvertsTopics.CODE_PUBLIC, AdvertsTopics.CODE_VERTICAL,
                    AdvertsTopics.CODE_VIDEO_CORNER, AdvertsTopics.CODE_VIDEO_DIALOG,
                    AdvertsTopics.CODE_ROAD), banner, dialog);


            NewAdvertsUtil.getInstance().setOnDialogSuccessLisinter(new NewAdvertsUtil
                    .OnDialogSuccessLisinter() {
                @Override
                public void success() {
                    NewAdvertsUtil.getInstance().showByDialog(CacheUtil.getDataList(AdvertsTopics
                            .CODE_HOME_DIALOG), dialog);
                }
            });

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
                    List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME);
                    if (dataList.size() == 0) {
                        NewAdvertsUtil.getInstance().showDefaultBanner(banner, 1);
                    } else {
                        NewAdvertsUtil.getInstance().showByBanner(CacheUtil.getDataList
                                (AdvertsTopics.CODE_HOME), banner);
                    }
                }
            });

            List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME);
            if (dataList.size() == 0) {
                NewAdvertsUtil.getInstance().showDefaultBanner(banner, 1);
            }
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
                mPresenter.getPatientData(String.valueOf(activeInfo.getBedId()),getActivity());
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
                start(HospitalMienFragment.getInstance());
                break;
            case R.id.ll_hostipal_ency:  // 医疗百科
                start(EncyclopediasFragment.getInstance());
                break;
            case R.id.ll_video:     // 视频
                start(VideoFragment.getIntance());
                break;
            case R.id.ll_hos_service:     //医疗服务

                break;
            case R.id.ll_video_line: // 直播TV
                start(new WoTvVideoLineFragment());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }
}

