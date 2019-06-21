package com.muju.note.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.adtask.contract.MainContract;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.adtask.presenter.MainPresenter;
import com.muju.note.launcher.app.bedsidecard.event.GotoBedsideEvent;
import com.muju.note.launcher.app.bedsidecard.ui.BedSideCardFragment;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.event.DrawOutEvent;
import com.muju.note.launcher.app.home.event.OutHospitalEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.home.util.PatientUtil;
import com.muju.note.launcher.app.luckdraw.ui.LuckDrawFragment;
import com.muju.note.launcher.app.msg.dialog.CustomMsgDialog;
import com.muju.note.launcher.app.publicui.AdvideoViewFragment;
import com.muju.note.launcher.app.publicui.LargePicFragment;
import com.muju.note.launcher.app.publicui.WebViewFragment;
import com.muju.note.launcher.app.publicui.ui.ProtectionProcessFragment;
import com.muju.note.launcher.app.satisfaction.event.GotoSatisfationEvent;
import com.muju.note.launcher.app.satisfaction.ui.SatisfactionSurveyFragment;
import com.muju.note.launcher.app.setting.event.GotoLuckEvent;
import com.muju.note.launcher.app.setting.event.GotoSignEvent;
import com.muju.note.launcher.app.sign.ui.SignTaskFragment;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.entity.AdvertWebEntity;
import com.muju.note.launcher.entity.BedSideEvent;
import com.muju.note.launcher.entity.PushAutoMsgEntity;
import com.muju.note.launcher.entity.PushCustomMessageEntity;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.EBDrawerLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity<MainPresenter> implements MainPresenter.TaskListener, MainContract.View {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
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
    @BindView(R.id.tv_bed_num)
    TextView tvBedNum;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_hos_name)
    TextView tvHosName;
    @BindView(R.id.tv_dept_name)
    TextView tvDeptName;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_nurse_name)
    TextView tvNurseName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_nurse_leven)
    TextView tvNurseLeven;
    @BindView(R.id.tv_dietCategory)
    TextView tvDietCategory;
    @BindView(R.id.drawlayout)
    EBDrawerLayout drawlayout;
    @BindView(R.id.ll_have_paitent)
    LinearLayout llHavePaitent;
    @BindView(R.id.tv_hos_dept_name)
    TextView tvHosDeptName;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.ll_no_patient)
    LinearLayout llNoPatient;
    @BindView(R.id.lly_draw)
    LinearLayout llDraw;
    private ActivePadInfo.DataBean activeInfo;

    private Disposable disposableProtection;
    private boolean isStartProtection = true;
    private static String TAG = "MainActivity";

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mPresenter.setOnTaskListener(this);
        EventBus.getDefault().register(this);
        startService(new Intent(this, MainService.class));

        activeInfo = ActiveUtils.getPadActiveInfo();
        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }


        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                // 登录沃tv
                WoTvUtil.getInstance().initSDK(LauncherApplication.getInstance());
            }
        });

        startProtectionCountDown();

        drawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                mPresenter.updateDate();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mPresenter.onDestroy();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        tvHosDeptName.setText(activeInfo.getHospitalName()+"-"+activeInfo.getDeptName()+"-"+activeInfo.getBedNumber() + "床");

        ivQrCode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID
                (getContext()) + "," + JPushInterface.getRegistrationID(getContext()), 200, 200));
    }

    @Override
    public void initPresenter() {
        mPresenter = new MainPresenter();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PatientInfoEvent event) {
        PatientResponse.DataBean info = event.info;
        setPatientInfo(info);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OutHospitalEvent event) {
        outHospital();
    }


    private void setPatientInfo(PatientResponse.DataBean entity) {
        PatientResponse.DataBean bean = PatientUtil.getInstance().getPatientData();
        if (bean == null) {
            LogUtil.i(TAG, "病人信息为空！");
            return;
        }
        tvBedNum.setText(activeInfo.getBedNumber());
        tvUserName.setText(bean.getUserName());
        tvAge.setText(bean.getAge() + "岁");
        tvHosName.setText("医院：" + activeInfo.getHospitalName());
        tvDeptName.setText("科室：" + activeInfo.getDeptName());
        tvDoctorName.setText("主治医师：" + bean.getChargeDoctor());
        tvNurseName.setText("责任护士：" + bean.getChargeNurse());
        tvDate.setText("日期：" + FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity.getCreateTime())));
        tvNurseLeven.setText("护理等级：" + bean.getNursingLevel());
        tvDietCategory.setText("饮食种类：" + bean.getDietCategory());
        tvSex.setText(bean.getSex() == 1 ? "男" : "女");

        llHavePaitent.setVisibility(View.VISIBLE);
        llNoPatient.setVisibility(View.GONE);
    }


    private void outHospital() {
        llNoPatient.setVisibility(View.VISIBLE);
        llHavePaitent.setVisibility(View.GONE);
    }

    /**
     * 开始倒计时
     */
    public void startProtectionCountDown() {
        RxUtil.closeDisposable(disposableProtection);
        if (isStartProtection) {
            protectionCountDown();
        }
    }

    /**
     * 屏幕保护倒计时
     */
    private void protectionCountDown() {
        long period = 3;
        disposableProtection = Observable.interval(period, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        start(new ProtectionProcessFragment(), SupportFragment.SINGLETASK);
                    }
                });

    }

    /**
     * 结束倒计时
     */
    public void stopProtectionCountDown() {
        RxUtil.closeDisposable(disposableProtection);
    }

    /**
     * 屏幕是否计时状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoNoLockEvent event) {
        LogFactory.l().i("event.VideoNoLockEvent==" + event.isLock);
        isStartProtection = event.isLock;
        if (!event.isLock) {
            stopProtectionCountDown();
        } else {
            startProtectionCountDown();
        }
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startProtectionCountDown();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //有触摸动作重置定时器
        startProtectionCountDown();
        return super.dispatchKeyEvent(event);
    }

    public void setStartProtection(boolean startProtection) {
        isStartProtection = startProtection;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 宣教推送
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushCustomMsg(PushCustomMessageEntity entity) {
        start(WebViewFragment.newInstance(entity.getTitle(), entity.getUrl(), entity.getCustomId(), true, 0));
    }

    /**
     * 宣教推送
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushAutoMsg(PushAutoMsgEntity entity) {
        CustomMsgDialog dialog = new CustomMsgDialog(this, entity.getMsg())
                .setCustView(R.layout.dialog_custom_msg_layout);
        dialog.show();
    }

    /**
     * 广告跳转
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAdverWebView(AdvertWebEntity entity) {
        switch (entity.getType()) {
            case 1:
                start(WebViewFragment.newInstance(entity.getTitle(), entity.getUrl(), entity.getAdvertId()));
                break;
            case 2:
                break;
            case 3:
                start(AdvideoViewFragment.newInstance(entity.getAdvertId(), entity.getUrl(), 0, 0));
                break;
            case 4:
                break;
            case 5:
                start(LargePicFragment.newInstance(entity.getTitle(), entity.getAdvertId(), entity.getUrl()));
                break;
        }

    }

    /**
     * 广告任务列表
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTaskList(UserInfoEvent userInfo) {
        int userId = userInfo.userBean.getId();
        int hospitalId = activeInfo.getHospitalId();
        int depId = activeInfo.getDeptId();
        mPresenter.getTaskList(userId, hospitalId, depId);
    }


    /**
     * 签到页面
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToSign(GotoSignEvent entity) {
        start(new SignTaskFragment());
    }

    /**
     * 抽奖页面
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToSign(GotoLuckEvent entity) {
        start(new LuckDrawFragment());
    }

    /**
     * 问卷调查
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToSign(GotoSatisfationEvent entity) {
        start(SatisfactionSurveyFragment.newInstance(entity.padsurvey));
    }

    /**
     * 侧边栏滑出
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void drawOut(DrawOutEvent event) {
       if(!drawlayout.isDrawerOpen(GravityCompat.END)){
            drawlayout.openDrawer(GravityCompat.END);
       }
    }

    /**
     * 问卷调查
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToBedSide(GotoBedsideEvent entity) {
        PatientResponse.DataBean info = entity.info;
        start(BedSideCardFragment.newInstance(info));
    }

    /**
     * 床头卡模式
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bedSide(BedSideEvent event) {
        if (event.getCode() == 13) {
            start(BedSideCardFragment.newInstance(HomeFragment.entity));
        } else {
            pop();
        }
    }

    @Override
    public void getTaskListSuccess(List<TaskListBean> taskListBeans) {
        SPUtil.saveDataList(Constants.AD_TASK_LIST, taskListBeans);
    }

    @Override
    public void getTaskListSuccess() {

    }

    @Override
    public void getDate(String time, String net, String netType) {
        tvTime.setText(time);
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
            ivWifi.setVisibility(View.GONE);
            ivNet.setVisibility(View.VISIBLE);
            tvNetType.setVisibility(View.VISIBLE);
            tvNetType.setText(netType);
            int netDbm = NetWorkUtil.getCurrentNetDBM(LauncherApplication.getContext());
            if (netDbm > -75) {
                ivNet.setImageResource(R.mipmap.net_level_good);
            } else if (netDbm > -85) {
                ivNet.setImageResource(R.mipmap.net_level_better);
            } else if (netDbm > -95) {
                ivNet.setImageResource(R.mipmap.net_level_normal);
            } else if (netDbm > -100) {
                ivNet.setImageResource(R.mipmap.net_level_bad);
            } else {
                ivNet.setImageResource(R.mipmap.net_level_none);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
