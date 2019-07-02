package com.muju.note.launcher.app.bedsidecard.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.bedsidecard.contract.BedsideContract;
import com.muju.note.launcher.app.bedsidecard.presenter.BedsidePresenter;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.net.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class BedSideCardFragment extends BaseFragment<BedsidePresenter> implements BedsideContract.View {
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_net)
    TextView tvNet;
    @BindView(R.id.tv_net_type)
    TextView tvNetType;
    @BindView(R.id.iv_net)
    ImageView ivNet;
    @BindView(R.id.iv_wifi)
    ImageView ivWifi;
    @BindView(R.id.lly_title)
    LinearLayout llyTitle;
    @BindView(R.id.tv_bed_num)
    TextView tvBedNum;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_card_num)
    TextView tvCardNum;
    @BindView(R.id.tv_card_time)
    TextView tvCardTime;
    @BindView(R.id.tv_card_info)
    TextView tvCardInfo;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_nurse)
    TextView tvNurse;
    @BindView(R.id.tv_hl)
    TextView tvHl;
    @BindView(R.id.iv_nurse)
    ImageView ivNurse;
    @BindView(R.id.tv_zb_nu)
    TextView tvZbNurse;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_hoipital)
    TextView tvHospital;
    @BindView(R.id.tv_food)
    TextView tvFood;
    private ActivePadInfo.DataBean activeInfo;
    private static final String BEDSIDE_INFO="bedside_info";
    private PatientResponse.DataBean info;

    @Override
    public int getLayout() {
        return R.layout.fragment_bedside_card;
    }

    public static BedSideCardFragment newInstance(PatientResponse.DataBean info) {
        Bundle args = new Bundle();
        args.putSerializable(BEDSIDE_INFO, info);
        BedSideCardFragment fragment = new BedSideCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.updateDate();
        EventBus.getDefault().post(new VideoNoLockEvent(false));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mPresenter.onDestroy();
        EventBus.getDefault().post(new VideoNoLockEvent(true));
    }

    @Override
    public void initData() {
        activeInfo = ActiveUtils.getPadActiveInfo();
        info = (PatientResponse.DataBean) getArguments().getSerializable(BEDSIDE_INFO);
        if(info!=null)
        setPatientInfo(info);
    }

    private void setPatientInfo(PatientResponse.DataBean entity){
        tvName.setText(entity.getUserName());
        tvAge.setText(entity.getAge() + "岁");
        tvDoctor.setText(entity.getChargeDoctor());
        tvNurse.setText(entity.getChargeNurse());
        tvCardTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity.getCreateTime())));
        tvBedNum.setText(activeInfo.getBedNumber());
        tvSex.setText(entity.getSex() == 1 ? "男" : "女");
        tvHl.setText(entity.getNursingLevel());
        tvZbNurse.setText(entity.getChargeNurse());
        tvCardNum.setText(activeInfo.getDeptName());
        tvHospital.setText(activeInfo.getHospitalName());
        tvFood.setText(entity.getDietCategory());
    }

    @Override
    public void initPresenter() {
        mPresenter=new BedsidePresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @Override
    public void getDate(String date, String time, String week, String net, String netType) {
        tvDate.setText(date);
        if(netType.equals("WIFI")){
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivNet.setVisibility(View.GONE);
            ivWifi.setVisibility(View.VISIBLE);
            int wifi=NetWorkUtil.getWifiLevel(LauncherApplication.getContext());
            if (wifi > -50 && wifi < 0) {//最强
                ivWifi.setImageResource(R.mipmap.white_wifi_level_good);
            } else if (wifi > -70 && wifi < -50) {//较强
                ivWifi.setImageResource(R.mipmap.white_wifi_level_better);
            } else if (wifi > -80 && wifi < -70) {//较弱
                ivWifi.setImageResource(R.mipmap.white_wifi_level_normal);
            } else if (wifi > -100 && wifi < -80) {//微弱
                ivWifi.setImageResource(R.mipmap.white_wifi_level_bad);
            }else {
                ivWifi.setImageResource(R.mipmap.white_wifi_level_none);
            }
        }else if(netType.equals("无网络连接") || netType.equals("未知")){
            ivWifi.setVisibility(View.VISIBLE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivWifi.setImageResource(R.mipmap.white_wifi_level_none);
            ivNet.setImageResource(R.mipmap.white_net_level_none);
        }else {
            ivWifi.setVisibility(View.GONE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            int netDbm=NetWorkUtil.getCurrentNetDBM(LauncherApplication.getContext());
            if (netDbm > -75) {
                ivNet.setImageResource(R.mipmap.white_net_level_good);
            } else if (netDbm > -85) {
                ivNet.setImageResource(R.mipmap.white_net_level_better);
            } else if (netDbm > -95) {
                ivNet.setImageResource(R.mipmap.white_net_level_normal);
            } else if (netDbm > -100) {
                ivNet.setImageResource(R.mipmap.white_net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.white_net_level_none);
            }
        }
    }
}
