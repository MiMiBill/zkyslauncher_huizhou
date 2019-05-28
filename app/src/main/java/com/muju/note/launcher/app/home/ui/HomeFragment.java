package com.muju.note.launcher.app.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.presenter.HomePresenter;
import com.muju.note.launcher.app.hostipal.ui.EncyclopediasFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.app.video.ui.VideoFragment;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ClickTimeUtils;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.banana.Banner;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    Unbinder unbinder;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_hos_service)
    LinearLayout llHosService;
    Unbinder unbinder1;

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
        initBanner();
        llHostipal.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llHostipalEncy.setOnClickListener(this);
    }


    //加载广告
    private void initBanner() {
        final AdvertsDialog dialog = new AdvertsDialog(getActivity(), R.style.dialog);
        try {
            NewAdvertsUtil.getInstance().queryAdverts(UIUtils.fun(AdvertsTopics.CODE_HOME,
                    AdvertsTopics.CODE_HOME_DIALOG,AdvertsTopics.CODE_LOCK,
                    AdvertsTopics.CODE_PUBLIC,AdvertsTopics.CODE_VERTICAL,
                    AdvertsTopics.CODE_VIDEO_CORNER,AdvertsTopics.CODE_VIDEO_DIALOG,
                    AdvertsTopics.CODE_ROAD),banner,dialog);



            NewAdvertsUtil.getInstance().setOnDialogSuccessLisinter(new NewAdvertsUtil.OnDialogSuccessLisinter() {
                @Override
                public void success() {
                    NewAdvertsUtil.getInstance().showByDialog(CacheUtil.getDataList(AdvertsTopics.CODE_HOME_DIALOG),dialog);
                }
            });

            NewAdvertsUtil.getInstance().setOnBannerFailLisinter(new NewAdvertsUtil.OnBannerFailLisinter() {
                @Override
                public void fail() {
                    NewAdvertsUtil.getInstance().showDefaultBanner(banner,1);
                }
            });

            NewAdvertsUtil.getInstance().setOnBannerSuccessLisinter(new NewAdvertsUtil.OnBannerSuccessLisinter() {
                @Override
                public void success() {
                    List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME);
                    if(dataList.size()==0){
                        NewAdvertsUtil.getInstance().showDefaultBanner(banner,1);
                    }else {
                        NewAdvertsUtil.getInstance().showByBanner(CacheUtil.getDataList(AdvertsTopics.CODE_HOME),banner);
                    }
                }
            });

            List<AdvertsBean> dataList = CacheUtil.getDataList(AdvertsTopics.CODE_HOME);
            if(dataList.size()==0){
                NewAdvertsUtil.getInstance().showDefaultBanner(banner,1);
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
        if(SPUtil.getBoolean("isSeekToPic")){
            banner.setCurrentItem(0);
            banner.startAutoPlay();
            SPUtil.putBoolean("isSeekToPic",false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SPUtil.putBoolean("isSeekToPic",true);
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
