package com.muju.note.launcher;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.adtask.presenter.MainPresenter;
import com.muju.note.launcher.app.home.bean.PatientResponse;
import com.muju.note.launcher.app.home.event.OutHospitalEvent;
import com.muju.note.launcher.app.home.event.PatientInfoEvent;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.msg.dialog.CustomMsgDialog;
import com.muju.note.launcher.app.publicui.ProtectionProcessFragment;
import com.muju.note.launcher.app.publicui.WebViewFragment;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.entity.AdvertWebEntity;
import com.muju.note.launcher.entity.PushAutoMsgEntity;
import com.muju.note.launcher.entity.PushCustomMessageEntity;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.log.LogFactory;
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
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity<MainPresenter> implements MainPresenter.TaskListener {

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

    private Disposable disposableProtection;
    private boolean isStartProtection = true;
    private static String TAG="MainActivity";
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mPresenter=new MainPresenter();
        mPresenter.setOnTaskListener(this);
        EventBus.getDefault().register(this);
        startService(new Intent(this, MainService.class));

        activeInfo = ActiveUtils.getPadActiveInfo();
        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }


        ExecutorService service= Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                // 登录沃tv
                WoTvUtil.getInstance().initSDK(LauncherApplication.getInstance());
            }
        });

        startProtectionCountDown();
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


    private void setPatientInfo(PatientResponse.DataBean entity){
        tvName.setText(entity.getUserName());
        tvAge.setText(entity.getAge() + "岁");
        tvHosDoctor.setText(entity.getChargeDoctor());
        tvHosTime.setText(FormatUtils.FormatDateUtil.parseLong(Long.parseLong(entity.getCreateTime())));
        tvBed.setText(activeInfo.getHospitalName()+"-"+activeInfo.getDeptName()+"-"+activeInfo.getBedNumber()+"床");
        tvPaitent.setText(entity.getSex() == 1 ? "男" : "女");
    }


    private void outHospital(){
        tvName.setText("");
        tvAge.setText("");
        tvHosDoctor.setText("");
        tvHosTime.setText("");
        tvBed.setText(activeInfo.getHospitalName()+"-"+activeInfo.getDeptName()+"-"+activeInfo.getBedNumber()+"床");
        tvPaitent.setText("");
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
     *  屏幕是否计时状态
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoNoLockEvent event) {
        LogFactory.l().i("event.VideoNoLockEvent==" + event.isLock);
        isStartProtection=event.isLock;
        if (!event.isLock) {
            stopProtectionCountDown();
        }else {
            startProtectionCountDown();
        }
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
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
     *  宣教推送
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushCustomMsg(PushCustomMessageEntity entity){
        start(WebViewFragment.newInstance(entity.getTitle(),entity.getUrl(),entity.getCustomId(),true,0));
    }

    /**
     *  宣教推送
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushAutoMsg(PushAutoMsgEntity entity){
        CustomMsgDialog dialog = new CustomMsgDialog(this, entity.getMsg())
                .setCustView(R.layout.dialog_custom_msg_layout);
        dialog.show();
    }

    /**
     *  广告跳转
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAdverWebView(AdvertWebEntity entity){
        start(WebViewFragment.newInstance(entity.getTitle(),entity.getUrl(),entity.getAdvertId()));
    }



    /**
     *  广告任务列表
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTaskList(UserInfoEvent userInfo){
        int userId=userInfo.userBean.getId();
        int hospitalId=activeInfo.getHospitalId();
        int depId=activeInfo.getDeptId();
        mPresenter.getTaskList(userId,hospitalId,depId);
    }

    @Override
    public void getTaskListSuccess(List<TaskListBean> taskListBeans) {
        SPUtil.saveDataList(Constants.AD_TASK_LIST,taskListBeans);
    }
}
