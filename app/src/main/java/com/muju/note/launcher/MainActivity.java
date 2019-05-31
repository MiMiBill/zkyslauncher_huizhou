package com.muju.note.launcher;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.view.EBDrawerLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawlayout)
    EBDrawerLayout drawlayout;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tv_bed)
    TextView tvBed;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_paitent)
    TextView tvPaitent;
    @BindView(R.id.tv_card_num)
    TextView tvCardNum;
    @BindView(R.id.tv_hos_time)
    TextView tvHosTime;
    @BindView(R.id.tv_hos_doctor)
    TextView tvHosDoctor;
    private ActivePadInfo.DataBean activeInfo;
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        startService(new Intent(this, MainService.class));
        activeInfo = ActiveUtils.getPadActiveInfo();
        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }

        // 登录沃tv
        WoTvUtil.getInstance().initSDK(LauncherApplication.getInstance());

        List<AdvertsBean> adverts = CacheUtil.getDataList(AdvertsTopics.CODE_VERTICAL);

        if (adverts != null && adverts.size() > 0) {
            NewAdvertsUtil.getInstance().showByImageView(adverts, ivAd);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PatientInfoEvent event) {
        PatientResponse.DataBean info = event.info;
        setPatientInfo(info);
    }


    private void setPatientInfo(PatientResponse.DataBean entity){
        tvName.setText(entity.getUserName());
        tvAge.setText(entity.getAge() + "岁");
        tvHosDoctor.setText(entity.getChargeDoctor());
        tvHosTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity
                .getCreateTime())));
        tvBed.setText(activeInfo.getBedNumber() + "床");
        tvPaitent.setText(activeInfo.getDeptName());
    }
}
