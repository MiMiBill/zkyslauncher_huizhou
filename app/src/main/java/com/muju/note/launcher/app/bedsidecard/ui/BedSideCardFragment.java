package com.muju.note.launcher.app.bedsidecard.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.bedsidecard.contract.BedsideContract;
import com.muju.note.launcher.app.bedsidecard.presenter.BedsidePresenter;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.event.OutHospitalEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.net.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

//床头卡
public class BedSideCardFragment extends BaseFragment<BedsidePresenter> implements
        BedsideContract.View {
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
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.lly_title)
    RelativeLayout llyTitle;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.iv_hl)
    ImageView ivHl;
    @BindView(R.id.iv_food)
    ImageView ivFood;
    @BindView(R.id.iv_bl)
    ImageView ivBl;
    @BindView(R.id.rel_nurse)
    RelativeLayout relNurse;
    @BindView(R.id.lly_no_info)
    LinearLayout llyNoInfo;
    @BindView(R.id.tv_gm)
    TextView tvGm;
    @BindView(R.id.tv_no_info)
    TextView tvNoInfo;


    private ActivePadInfo.DataBean activeInfo;
    private static final String BEDSIDE_INFO = "bedside_info";
    private static final String BEDSIDE_ISPUSH = "bedside_ispush";
    private PatientResponse.DataBean info;
    private boolean isPush = true;
    private String netType = "";
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

    @Override
    public int getLayout() {
        return R.layout.fragment_bedside_card;
    }

    public static BedSideCardFragment newInstance(PatientResponse.DataBean info, boolean isPush) {
        Bundle args = new Bundle();
        args.putSerializable(BEDSIDE_INFO, info);
        args.putBoolean(BEDSIDE_ISPUSH, isPush);
        BedSideCardFragment fragment = new BedSideCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        mPresenter.updateDate();
//        EventBus.getDefault().post(new VideoNoLockEvent(false));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mPresenter.onDestroy();
        handler.removeMessages(1);
//        EventBus.getDefault().post(new VideoNoLockEvent(true));
    }

    @Override
    public void initData() {
        activeInfo = ActiveUtils.getPadActiveInfo();
        info = (PatientResponse.DataBean) getArguments().getSerializable(BEDSIDE_INFO);
        isPush = getArguments().getBoolean(BEDSIDE_ISPUSH);

        setHosiptal();

        if (info != null) {
            setPatientInfo(info);
        } else {
            outHospital();
        }


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        if (isPush) {
            llBack.setVisibility(View.GONE);
        } else {
            llBack.setVisibility(View.VISIBLE);
        }
    }


    //出院
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OutHospitalEvent event) {
        outHospital();
    }

    //入院
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PatientInfoEvent event) {
        PatientResponse.DataBean info = event.info;
        setPatientInfo(info);
    }

    //出院信息
    private void outHospital() {
        if (tvName != null) {
            String text = getResources().getString(R.string.no_info);
            tvName.setText(text);
            tvName.setTextSize(getResources().getDimension(R.dimen.px40));
            tvAge.setText(text);
            tvDoctor.setText(text);
            tvNurse.setText(text);
            tvCardTime.setText(text);
            tvSex.setVisibility(View.GONE);
            tvHl.setText(text);
            tvZbNurse.setText(text);
            tvFood.setText(text);
            tvName.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvAge.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvAge.setTextSize(getResources().getDimension(R.dimen.px40));
            tvDoctor.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvNurse.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvCardTime.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvHl.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvZbNurse.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvFood.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvGm.setText(getResources().getString(R.string.no_info));
            tvGm.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            tvNoInfo.setTextColor(getResources().getColor(R.color.color_0B8CD1));
            relNurse.setVisibility(View.GONE);
            llyNoInfo.setVisibility(View.VISIBLE);
            tvHint.setText("请联系护士登录");
            ivHl.setImageResource(R.mipmap.card_hl_none);
            ivFood.setImageResource(R.mipmap.card_food_none);
            ivBl.setImageResource(R.mipmap.card_gm_none);
        } else {
            LogFactory.l().i("tvName==null");
        }

    }

    private void setHosiptal() {
        tvBedNum.setText(activeInfo.getBedNumber());
        tvCardNum.setText(activeInfo.getDeptName());
        tvHospital.setText(activeInfo.getHospitalName());
    }

    private void setPatientInfo(PatientResponse.DataBean entity) {
        if (tvName != null) {
            tvName.setText(entity.getUserName());
            tvName.setTextSize(getResources().getDimension(R.dimen.px70));
            tvAge.setText(entity.getAge() + "岁");
            tvDoctor.setText(entity.getChargeDoctor());
            tvNurse.setText(entity.getChargeNurse());
            tvCardTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity
                    .getCreateTime())));
            tvSex.setVisibility(View.VISIBLE);
            tvSex.setTextSize(getResources().getDimension(R.dimen.px70));
            tvSex.setText(entity.getSex() == 1 ? "男" : "女");
            tvHl.setText(entity.getNursingLevel());
            tvZbNurse.setText(entity.getChargeNurse());
            tvFood.setText(entity.getDietCategory());
            tvName.setTextColor(getResources().getColor(R.color.white));
            tvAge.setTextColor(getResources().getColor(R.color.white));
            tvDoctor.setTextColor(getResources().getColor(R.color.white));
            tvNurse.setTextColor(getResources().getColor(R.color.white));
            tvCardTime.setTextColor(getResources().getColor(R.color.white));
            tvSex.setTextColor(getResources().getColor(R.color.white));
            tvHl.setTextColor(getResources().getColor(R.color.white));
            tvZbNurse.setTextColor(getResources().getColor(R.color.white));
            tvFood.setTextColor(getResources().getColor(R.color.white));
            tvHint.setText(getResources().getString(R.string.card_notice));
            tvGm.setText(getResources().getString(R.string.no_info));
            tvGm.setTextColor(getResources().getColor(R.color.white));

            relNurse.setVisibility(View.VISIBLE);
            llyNoInfo.setVisibility(View.GONE);
            ivHl.setImageResource(R.mipmap.card_hl);
            ivFood.setImageResource(R.mipmap.card_food);
            ivBl.setImageResource(R.mipmap.card_gm);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new BedsidePresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @Override
    public void getDate(String date, String time, String week, String net, String netType) {
        tvDate.setText(date);
        if (netType.equals("WIFI")) {
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivNet.setVisibility(View.GONE);
            ivWifi.setVisibility(View.VISIBLE);
            int wifi = NetWorkUtil.getWifiLevel(LauncherApplication.getContext());
            if (wifi > -50 && wifi < 0) {//最强
                ivWifi.setImageResource(R.mipmap.white_wifi_level_good);
            } else if (wifi > -70 && wifi < -50) {//较强
                ivWifi.setImageResource(R.mipmap.white_wifi_level_better);
            } else if (wifi > -80 && wifi < -70) {//较弱
                ivWifi.setImageResource(R.mipmap.white_wifi_level_normal);
            } else if (wifi > -100 && wifi < -80) {//微弱
                ivWifi.setImageResource(R.mipmap.white_wifi_level_bad);
            } else {
                ivWifi.setImageResource(R.mipmap.white_wifi_level_none);
            }
        } else if (netType.equals("无网络连接") || netType.equals("未知")) {
            ivWifi.setVisibility(View.VISIBLE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            ivWifi.setImageResource(R.mipmap.white_wifi_level_none);
            ivNet.setImageResource(R.mipmap.white_net_level_none);
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
                ivNet.setImageResource(R.mipmap.white_net_level_good);
            } else if (netDbm > -110) {
                ivNet.setImageResource(R.mipmap.white_net_level_better);
            } else if (netDbm > -125) {
                ivNet.setImageResource(R.mipmap.white_net_level_normal);
            } else if (netDbm > -140) {
                ivNet.setImageResource(R.mipmap.white_net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.white_net_level_none);
            }
        } else {
            if (netDbm > -75) {
                ivNet.setImageResource(R.mipmap.white_net_level_good);
            } else if (netDbm > -90) {
                ivNet.setImageResource(R.mipmap.white_net_level_better);
            } else if (netDbm > -105) {
                ivNet.setImageResource(R.mipmap.white_net_level_normal);
            } else if (netDbm > -120) {
                ivNet.setImageResource(R.mipmap.white_net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.white_net_level_none);
            }
        }
    }

}
