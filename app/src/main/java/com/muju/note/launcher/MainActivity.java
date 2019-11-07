package com.muju.note.launcher;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentationMagician;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.app.Cabinet.ui.CabinetFragment;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.adtask.contract.MainContract;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.adtask.presenter.MainPresenter;
import com.muju.note.launcher.app.bedsidecard.ui.BedSideCardFragment;
import com.muju.note.launcher.app.finance.FinanceFragment;
import com.muju.note.launcher.app.game.ui.GameFragment;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.event.DefaultCabinetEvent;
import com.muju.note.launcher.app.home.event.DefaultVideoEvent;
import com.muju.note.launcher.app.home.event.DefaultVideoLiveEvent;
import com.muju.note.launcher.app.home.event.DrawOutEvent;
import com.muju.note.launcher.app.home.event.OutHospitalEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.home.util.PatientUtil;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.app.hostipal.ui.HosPitalMissionFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalEncyFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMissionPdfFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMissionVideoFragment;
import com.muju.note.launcher.app.hostipal.ui.MissionPushDialog;
import com.muju.note.launcher.app.insurance.InsureanceFragment;
import com.muju.note.launcher.app.luckdraw.ui.LuckDrawFragment;
import com.muju.note.launcher.app.msg.db.CustomMessageDao;
import com.muju.note.launcher.app.msg.dialog.CustomMsgDialog;
import com.muju.note.launcher.app.orderfood.ui.OrderFoodFragment;
import com.muju.note.launcher.app.publicui.AdvideoViewFragment;
import com.muju.note.launcher.app.publicui.LargePicFragment;
import com.muju.note.launcher.app.publicui.WebViewFragment;
import com.muju.note.launcher.app.publicui.ui.ProtectionProcessFragment;
import com.muju.note.launcher.app.satisfaction.event.GotoSatisfationEvent;
import com.muju.note.launcher.app.satisfaction.ui.SatisfactionSurveyFragment;
import com.muju.note.launcher.app.setting.event.GotoLuckEvent;
import com.muju.note.launcher.app.setting.event.GotoSignEvent;
import com.muju.note.launcher.app.shop.ShopFragment;
import com.muju.note.launcher.app.sign.ui.SignTaskFragment;
import com.muju.note.launcher.app.timetask.CrontabService;
import com.muju.note.launcher.app.timetask.event.TimeTaskEvent;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.ui.VideoFragment;
import com.muju.note.launcher.app.video.ui.WoTvVideoLineFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.entity.AdvertWebEntity;
import com.muju.note.launcher.entity.BedSideEvent;
import com.muju.note.launcher.entity.PushAutoMsgEntity;
import com.muju.note.launcher.entity.PushCustomMessageEntity;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.service.recordLog.RecordLogService;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.EBDrawerLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
    private String netType = "";
    private Disposable disposableProtection;
    private boolean isStartProtection = true;
    private int clickCount=0;
    private boolean isRecordLog=true;
    private static String TAG = "MainActivity";
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
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

        Set<String> tags = new HashSet<>();
        tags.add("平板桌面");
        JPushInterface.setTags(LauncherApplication.getContext(),2,tags);


        mPresenter.setOnTaskListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        startService(new Intent(this, MainService.class));

        activeInfo = ActiveUtils.getPadActiveInfo();
        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
//            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());

            loadRootFragment(R.id.fl_container, HomeFragment.newInstance(), false, false);

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

        tvHosDeptName.setText(activeInfo.getHospitalName() + "-" + activeInfo.getDeptName() + "-" + activeInfo.getBedNumber() + "床");

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
        CrontabService.getInstance().cancleAlarm(this); //取消定时事件
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
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ++clickCount;
                    startProtectionCountDown();
                    if(clickCount>=6 && isRecordLog){
                        isRecordLog=false;
                        //平板操作日志
                        RecordLogService.getInstance().start();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        handler.removeMessages(1);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 宣教推送
     *
     * @param entity
     */
    private MissionPushDialog pushDialog;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushCustomMsg(final PushCustomMessageEntity entity) {
        LogUtil.d(TAG, "宣教推送ID：" + entity.getId());
        LitePalDb.setZkysDb();
        LitePal.where("missionid=?", entity.getId()).findFirstAsync(MissionInfoDao.class).listen(new FindCallback<MissionInfoDao>() {
            @Override
            public void onFinish(final MissionInfoDao missionInfoDao) {
                if (missionInfoDao == null) {
                    return;
                }
                if(entity.getPushType()==1){
                    toMission(missionInfoDao,entity.getPushId());
                }else {
                    pushDialog = new MissionPushDialog(MainActivity.this, R.style.DialogFullscreen, missionInfoDao.getImg(), missionInfoDao.getTitle(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.btn_toMission) {
                                pushDialog.dismiss();
                                toMission(missionInfoDao,entity.getPushId());
                            }
                        }
                    });
                    pushDialog.setCanceledOnTouchOutside(false);
                    pushDialog.show();
                }

                mPresenter.getUpdateArriveFlag(entity.getPushId());
            }
        });
    }

    /**
     *  到宣教页面
     * @param missionInfoDao
     */
    private void toMission(MissionInfoDao missionInfoDao,String pushId) {
        String type;
        if (TextUtils.isEmpty(missionInfoDao.getVideo())) {
            File file = new File(SdcardConfig.RESOURCE_FOLDER, missionInfoDao.getFrontCover().hashCode() + ".pdf");
            if (!file.exists()) {
                showToast("正在下载中，请稍后重试");
                MissionService.getInstance().downMission();
                return;
            }
            start(HospitalMissionPdfFragment.newInstance(missionInfoDao.getFrontCover(),missionInfoDao.getMissionId(),missionInfoDao.getTitle(),pushId));
            type = "pdf";
        } else {
            File file = new File(SdcardConfig.RESOURCE_FOLDER, missionInfoDao.getVideo().hashCode() + ".mp4");
            if (!file.exists()) {
                showToast("正在下载中，请稍后重试");
                MissionService.getInstance().downMission();
                return;
            }
            start(HospitalMissionVideoFragment.newInstance(missionInfoDao.getVideo(),missionInfoDao.getMissionId(),missionInfoDao.getTitle(),pushId));
            type = "video";
        }

        CustomMessageDao dao = new CustomMessageDao();
        dao.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
        dao.setCreateTime(System.currentTimeMillis());
        dao.setType(type);
        dao.setTitle(missionInfoDao.getTitle());
        dao.setMsgId(missionInfoDao.getMissionId());
        if ("pdf".equals(type)) {
            dao.setUrl(missionInfoDao.getFrontCover());
        } else if ("video".equals(type)) {
            dao.setUrl(missionInfoDao.getVideo());
        }
        LitePalDb.setZkysDb();
        dao.save();
    }

    /**
     * 自定义消息推送
     *
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushAutoMsg(PushAutoMsgEntity entity) {
        CustomMsgDialog dialog = new CustomMsgDialog(this, entity.getMsg()).setCustView(R.layout.dialog_custom_msg_layout);
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
                start(new GameFragment());
                break;
            case 3:
                start(AdvideoViewFragment.newInstance(entity.getAdvertId(), entity.getUrl(), 0, 0));
                break;
            case 4:
                switch (entity.getUrl()) {   //模块跳转
                    case AdvertsTopics.MODE_HOSPITAL:
                        start(new HospitalMienFragment());
                        break;
                    case AdvertsTopics.MODE_MISSION:
                        start(new HosPitalMissionFragment());
                        break;
                    case AdvertsTopics.MODE_EGANCE:
                        start(new HospitalEncyFragment());
                        break;
                    case AdvertsTopics.MODE_CABINET:
                        start(new CabinetFragment());
                        break;
                    case AdvertsTopics.MODE_VIDEO:      //影视
                        start(new VideoFragment());
                        break;
                    case AdvertsTopics.MODE_GAME:
                        start(new GameFragment());
                        break;
                    case AdvertsTopics.MODE_LIVE:
                        start(new HospitalMienFragment());
                        break;
                    case AdvertsTopics.MODE_SHOPPING:
                        start(new ShopFragment());
                        break;
                    case AdvertsTopics.MODE_FINANCE:
                        start(new FinanceFragment());
                        break;
                    case AdvertsTopics.MODE_INSUREANCE:
                        start(new InsureanceFragment());
                        break;
                    case AdvertsTopics.MODE_ORDER:
                        start(new OrderFoodFragment());
                        break;
                }
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
     * 影视
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToVideo(DefaultVideoEvent event) {
        start(new VideoFragment());
    }

    /**
     * 屏安柜
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToCabinet(DefaultCabinetEvent event) {
        start(new CabinetFragment());
    }


    /**
     * 直播
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToVideoLive(DefaultVideoLiveEvent event) {
        start(new WoTvVideoLineFragment());
    }

    /**
     * 侧边栏滑出
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void drawOut(DrawOutEvent event) {
        if (!drawlayout.isDrawerOpen(GravityCompat.END)) {
            drawlayout.openDrawer(GravityCompat.END);
        }
    }

    /**
     * 床头卡模式
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bedSide(BedSideEvent event) {
        LogUtil.d(TAG, "bedCode:" + event.getCode());
        if (event.getCode() == 13) {
            start(BedSideCardFragment.newInstance(HomeFragment.entity, true));
        } else {
            popToHome();
        }
    }


    /**
     * 收到/取消定时事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void timeTask(TimeTaskEvent event) {
        if (event.getCode() == 15) {
            CrontabService.getInstance().cancleAlarm(this);
            CrontabService.getInstance().start();
        } else {
            CrontabService.getInstance().cancleAlarm(this);
        }
    }


    private void popToHome() {
        try {
            List<Fragment> fragmentList = FragmentationMagician.getActiveFragments(this.getSupportFragmentManager());
            for (Fragment fragment : fragmentList) {
                if (!"HomeFragment".equals(fragment.getClass().getSimpleName())) {
                    pop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
