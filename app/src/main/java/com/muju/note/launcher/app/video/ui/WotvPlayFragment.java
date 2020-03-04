package com.muju.note.launcher.app.video.ui;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.api.video.impl.VideoErrorInfo;
import com.devbrackets.android.media.listener.OnVideoPreparedListener;
import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.PriceBean;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.bean.WeiXinTask;
import com.muju.note.launcher.app.video.contract.VideoPlayContract;
import com.muju.note.launcher.app.video.db.PayInfoDao;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.app.video.dialog.NewVideoPayDialog;
import com.muju.note.launcher.app.video.dialog.VideoOrImageDialog;
import com.muju.note.launcher.app.video.dialog.WotvPlayErrorDialog;
import com.muju.note.launcher.app.video.event.VideoCodeFailEvent;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.event.VideoPauseEvent;
import com.muju.note.launcher.app.video.event.VideoReStartEvent;
import com.muju.note.launcher.app.video.event.VideoStartEvent;
import com.muju.note.launcher.app.video.presenter.VideoPlayPresenter;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.app.video.util.wotv.ExpandVideoView2;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.service.config.ConfigService;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.adverts.AdvertsUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.toast.FancyToast;
import com.muju.note.launcher.util.toast.ToastUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.password.OnPasswordFinish;
import com.muju.note.launcher.view.password.PopEnterPassword;
import com.unicom.common.VideoSdkConfig;
import com.unicom.common.base.video.IVideoEvent;
import com.unicom.common.base.video.expand.ExpandVideoListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.leefeng.promptlibrary.PromptDialog;
import pl.droidsonroids.gif.GifImageView;

public class WotvPlayFragment extends BaseFragment<VideoPlayPresenter> implements
        VideoPlayContract.View, View.OnClickListener {
    @BindView(R.id.video_view)
    ExpandVideoView2 videoView;
    @BindView(R.id.iv_loading)
    GifImageView ivLoading;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.ll_des)
    LinearLayout llDes;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_corner)
    ImageView ivCorner;
    @BindView(R.id.iv_colse)
    ImageView ivColse;
    @BindView(R.id.rel_cornor)
    RelativeLayout relCornor;
    private boolean isCodeFail = false;
    private static final String TAG = "WotvPlayFragment";
    private boolean isShowDialog = true;
    private boolean isPaySuccess = false;
    /**
     * 视频类型
     * 1：电视台直播
     * 2：连续剧
     * 3：电影
     * 4：综艺
     * 5：体育
     * 6：VR
     * 7：今日看啥
     * 8：短视频
     */
    public static final int VIDEO_TYPE_EPISODE = 2;
    public static final int VIDEO_TYPE_VARIETY = 4;
    private VideoOrImageDialog videoOrImageDialog;
    // 当前播放的集数
    private static int EPISODE_POSITION;
    private VideoHisDao videoHisDao;
    private Disposable diPayDialog;
    private long startTime;
    private WotvPlayErrorDialog errorDialog;
    private ActivePadInfo.DataBean activeInfo;
    private List<PriceBean.DataBean> priceList = new ArrayList<>();
    private NewVideoPayDialog newVideoPayDialog;
    private LoginDialog loginDialog;
    private PopEnterPassword popEnterPassword;
    private static WeiXinTask.WeiXinTaskData weiXinTaskData;
    private  PromptDialog promptDialog;


    public void setHisDao(VideoHisDao videoHisDao) {
        this.videoHisDao = videoHisDao;
    }

    private int dialogType = 0;  //0表示有公众号任务
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == SHOW_PAY_DIALOG) {
                    diPayDialog = Observable.just(1)
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(Integer integer) throws Exception {
                                    mPresenter.setPayPackageList();
                                }
                            });
                }
                if (msg.what == 0x01) {
                    if (llDes == null) {
                        return;
                    }
                    llDes.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragment_wotv_play;
    }

    @Override
    public void initData() {
        try {
            EventBus.getDefault().register(this);
            activeInfo = ActiveUtils.getPadActiveInfo();
            startTime = System.currentTimeMillis();
            ivBack.setOnClickListener(this);

            tvName.setText(videoHisDao.getName());
            tvDes.setText(videoHisDao.getDescription());

            handler.sendEmptyMessageDelayed(0x01, 1000 * 15);

            // 设置播放器回调
            setVideoView();

            // 获取当前播放的集数
            EPISODE_POSITION = videoView.getEpisodePosition();
            // 开始播放
            playVideoAndSetUI();

            // 监听支付
            checkIsValid();
            // 视频开始播放后，展示上下菜单
            videoView.setOnPreparedListener(new OnVideoPreparedListener() {
                @Override
                public void onPrepared(boolean b) {
                    try {
                        videoView.setBasicControlDialogsVisible(true, true);
                        llLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // 添加历史记录
            videoHisDao.setCreateTime(System.currentTimeMillis() + "");
            VideoService.getInstance().addVideoHisInfo(videoHisDao);

            verifyPlayingStatus();

            // 设置免费时长事件
            if(ConfigService.VIDEO_PAY_TIME==0){
                pay_cuntDown = 60 * 1000 * 16;
            }else {
                pay_cuntDown=(ConfigService.VIDEO_PAY_TIME*60*1000);
            }

            LogUtil.d(TAG,"免费影视时长："+pay_cuntDown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new VideoPlayPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                isShowDialog = false;
                videoView.pause();
                pop();
                break;
        }
    }

    /**
     * 界面可见时
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        EventBus.getDefault().post(new VideoNoLockEvent(false));
    }

    /**
     * 界面不可见时
     */
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (newVideoPayDialog != null && newVideoPayDialog.isShowing()) {
            LogUtil.i(TAG, "支付窗口弹出，不发送锁屏消息");
            newVideoPayDialog.dismiss();
            return;
        }
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
            return;
        }

        if (popEnterPassword != null && popEnterPassword.isShowing())
            popEnterPassword.dismiss();
//        videoView.unregisterVideoTouchEventObserver(observer);
        EventBus.getDefault().post(new VideoNoLockEvent(true));

        if(videoView!=null){
            videoView.pause();
        }

    }

    /**
     * 播放器回调监听
     */
    private void setVideoView() {
        //播放器的播放过程，状态回调
        videoView.setVideoEvent(new IVideoEvent() {
            @Override
            public void setPosterVisiable(boolean isVisiable) {
                try {
                    verifyPlayingStatus();
                    //TODO 播放器的默认海报展示的时候，会通知业务层，由业务层处理业务逻辑。
                    Log.e(TAG, "海报是否显示：" + isVisiable);
                    if (videoView == null) {
                        return;
                    }
                    if (videoView != null && !videoView.isPlaying()) {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void changeScreenOrientation(boolean isFullScreen) {
                //TODO 切换横竖屏、大小屏的核心实现，完成此方法，即可，不同合作方根据自己的业务实现，当前demo仅为测试样例
                //todo 加点切换的动画
                LogUtil.e(TAG, "changeScreenOrientation:" + isFullScreen);
            }

            @Override
            public void onControlsVisiable(boolean isShown) {
                //TODO 播放器默认的控制层是否展示状态，如果是无UI模式，则不需要理会此通知
                LogUtil.d("VideoPlayer:isShown %s", isShown);
            }

            @Override
            public boolean onVideoComplete() {
                try {
                    //TODO 视频播放完成（一个视频达到duration的末尾），通知业务层
                    LogUtil.e(TAG, "onVideoComplete:");
                    switch (videoHisDao.getVideoType()) {
                        case VIDEO_TYPE_EPISODE:
                        case VIDEO_TYPE_VARIETY:
                            EPISODE_POSITION = videoView.getEpisodePosition();
                            //自动播放下一集
                            if (EPISODE_POSITION < videoView.getVideoEpisodes().size() - 1) {
                                EPISODE_POSITION++;
                                videoView.changeEpisode(EPISODE_POSITION);
                            } else {
                                //todo 如果最后一集就应该播放另一部视频
                                showToast("视频已播放完成");
                                pop();
                            }

                            break;
                        default:
                            showToast("视频已播放完成");
                            pop();
                            break;
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    pop();
                }
                return true;
            }

            @Override
            public void onCollectedVideo(boolean isCollected) {
                //TODO 视频是否被收藏，配套沃视频的功能，可不实现，不影响
            }

            @Override
            public void onError(VideoErrorInfo.VideoException e) {
                try {
                    LogUtil.e(TAG, "onError:" + e.toString());
                    //TODO VideoErrorInfo配套查阅错误代码
                    LogUtil.e(TAG, "错误代码：" + e.getCode());
                    LogUtil.e(TAG, "错误信息：" + e.getMessage());
                    if (errorDialog != null && errorDialog.isShowing()) {
                        return;
                    }
                    errorDialog = new WotvPlayErrorDialog(getActivity(), R.style
                            .DialogFullscreen, e.getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.dismiss();
                            pop();
                        }
                    });
                    errorDialog.setCanceledOnTouchOutside(false);
                    errorDialog.show();
                    RxUtil.closeDisposable(diVerifyPlayingStatus);
                    switch (e.getCode()) {
                        case VideoErrorInfo.CODE_ACCOUNT_CHECK_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_CONTENTE_PERMISSION:
                            if (VideoSdkConfig.getInstance().getUser().isLogined()) {
                                //这里发生错误就先重新登录
                                errorDialog.setErrorMsg("错误信息：没有权限播放此视频");
                            } else {
                                WoTvUtil.getInstance().login();
                            }
                            break;
                        case VideoErrorInfo.CODE_VIDEO_INNER_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_GET_ERROR:
                            break;
                        case VideoErrorInfo.CODE_ORDER_CHECK_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_URL_ERROR:
                            WoTvUtil.getInstance().login();
                            break;
                    }
                } catch (Exception es) {
                    es.printStackTrace();
                    pop();
                    if (errorDialog != null && errorDialog.isShowing()) {
                        errorDialog.dismiss();
                    }
                }
            }


        });
    }

    /**
     * 播放回调函数，包括  播放完成，点击视频区域，播放异常
     */
    private ExpandVideoListener mControlListener = new ExpandVideoListener() {

        @Override
        public void onBeforeStart(boolean isFreeFlow, boolean isFreeContent) {
            //如果进入该回调，那么视频正常播放
            LogUtil.e("INFO  %s", "该账号下，该视频是否全部清晰度免流量：" + isFreeFlow);
            LogUtil.e("INFO  %s", "该账号下，该视频是否内容免费： " + isFreeContent);
            if (!isFreeContent) {
                //可删除或用log替换，调试用
                LogUtil.e("该账号下，该视频内容不免费，无法播放，请添加主动切换下一个轮播节目逻辑");
                //模拟切换
                // switchContent();
            }
            verifyPlayingStatus();
        }

        @Override
        public void onPrepare(List<Integer> list, int selectedDefinition) {

        }


        @Override
        public void onVideoShieldStart(int i, String s) {
            //可删除或用log替换，调试用
            LogUtil.e("版权时段，请切换下一个视频源");
        }

        @Override
        public void onVideoShieldEnd() {
            //此回调不处理
        }
    };

    /**
     * 校验播放状态，避免视频无法播放
     */
    Disposable diVerifyPlayingStatus;

    private void verifyPlayingStatus() {
        LogUtil.d("开始重新播放监测");
        RxUtil.closeDisposable(diVerifyPlayingStatus);
        diVerifyPlayingStatus = Observable.timer(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            if (videoView == null) {
                                return;
                            }
                            long playedDuration = videoView.getPlayedDuration();
                            long currentPosition = videoView.getCurrentPosition();
                            if (playedDuration == 0 && currentPosition == 0 && !videoView
                                    .isPlaying()) {
                                LogUtil.d("videoView.重新播放() :%s", "调用重新播放,开启重新监听");
                                playVideoAndSetUI();
                                videoView.restart();
                                verifyPlayingStatus();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void playVideoAndSetUI() {
        switchContentWithCid(videoHisDao.getCid(), videoHisDao.getPlayType());
    }

    /**
     * 根据内容id 切换视频
     *
     * @param cid
     * @param videoType
     */
    public void switchContentWithCid(final String cid, final String videoType) {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (videoView == null) return;
                        LogUtil.e("switchContentWithCid", "cid:" + cid);
                        WoTvUtil.getInstance().switchContent(videoView, cid, videoType, null,
                                videoHisDao.getName(), mControlListener, 1);
                    }
                });
    }


    //监听视频开始播放
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoStartEvent event) {
//        LogFactory.l().i("VideoStartEvent==" + event.isStart);
        if (event.isStart) {
            if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                videoOrImageDialog.dismiss();
            }
            LitePalDb.setZkysDb();
            LitePal.where("code =?", AdvertsTopics.CODE_VIDEO_CORNER).
                    limit(1).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
                @Override
                public void onFinish(List<AdvertsCodeDao> list) {
                    if (list != null && list.size() > 0) {
                        AdvertsCodeDao codeDao = list.get(0);
                        if (codeDao != null && (!codeDao.getResourceUrl().equals(""))) {
                            AdvertsUtil.getInstance().showByImageView(getActivity(), codeDao,
                                    ivCorner, ivColse, relCornor);
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (videoView != null && (!isCodeFail)) {
//            LogFactory.l().e("onStart");
            videoView.onStart();
        }
    }

    @Override
    public void onPause() {
        if (videoView != null) {
            videoView.onPause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null && (!isCodeFail)) {
//            LogFactory.l().e("onResume");
            if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
//                LogFactory.e(" dialog != null : " + videoOrImageDialog.isShowing());
                videoView.pause();
            } else {
                videoView.onResume();
            }
        }
    }

    //监听视频重新开始播放
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoReStartEvent event) {
//            LogFactory.l().i("event.VideoReStartEvent==" + event.isStart);
        if (event.isStart) {
            videoView.start();
            newVideoPayDialog.dismiss();
        }
    }

    //监听失败返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoCodeFailEvent event) {
//            LogFactory.l().i("event.VideoCodeFailEvent==" + event.isFail);
        isCodeFail = event.isFail;
        if (event.isFail) {
            videoView.pause();
        }
    }

    //监听视频播放暂停
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoPauseEvent event) {
//        LogFactory.l().i("VideoPauseEvent==" + event.isPause);
        if (event.isPause && isShowDialog) {
            LitePalDb.setZkysDb();
            LitePal.where("code =?", AdvertsTopics.CODE_VIDEO_DIALOG).limit(1).findAsync
                    (AdvertsCodeDao.class).
                    listen(new FindMultiCallback<AdvertsCodeDao>() {
                        @Override
                        public void onFinish(List<AdvertsCodeDao> list) {
                            if (list != null && list.size() > 0) {
                                AdvertsCodeDao codeDao = list.get(0);
                                try {
                                    if (videoOrImageDialog == null) {
                                        videoOrImageDialog = new VideoOrImageDialog(getActivity()
                                                , R.style.dialog);
                                        if (codeDao != null) {
                                            AdvertsUtil.getInstance().showVideoDialog(codeDao,
                                                    videoOrImageDialog);
                                        }
                                    } else {
                                        if (codeDao != null && codeDao.getCloseType() == 2) {
                                            videoOrImageDialog.closeBySelf(codeDao.getSecond());
                                        }
                                        videoOrImageDialog.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }


    /**
     * 检查是否需要支付
     */
    private Disposable disposableIsValid;
    private long pay_cuntDown = 60 * 1000 * 16;//倒计时收费
    public static final int SHOW_PAY_DIALOG = 0X123;

    private void checkIsValid() {
        disposableIsValid = Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            if (videoView == null) {
                                return;
                            }

                            if (videoView.getPlayedDuration() != 0 && videoView.getCurrentPosition() != 0) {
                                if (videoView.getCurrentPosition() > pay_cuntDown || videoView
                                        .getPlayedDuration() > pay_cuntDown) {
                                    handler.sendEmptyMessage(SHOW_PAY_DIALOG);
                                    RxUtil.closeDisposable(disposableIsValid);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }





    /**
     * 展示支付信息
     */
    private void showPayDialog() {

        if (promptDialog == null)
        {
            promptDialog = new PromptDialog(getActivity());
        }
        LitePalDb.setZkysDb();
        LitePal.deleteAll(PayInfoDao.class);
        //暂停播放
        if (videoView != null) {
            videoView.pause();
        }

        if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
            videoOrImageDialog.dismiss();
        }
        //开启轮询
        selectPayInterval();

//        LitePalDb.setZkysDb();
//        LitePal.where("taskType =?", "1").limit(1).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
//            @Override
//            public void onFinish(List<AdvertsCodeDao> list) {
//                if (list != null && list.size() > 0) {
//                    AdvertsCodeDao codeDao = list.get(0);
//                    if (codeDao != null && (!codeDao.getTaskUrl().equals(""))) {
//                        dialogType = 0;
//                    } else {
//                        dialogType = 1;
//                    }
//                }
//            }
//        });
        if (activeInfo != null) {
            mPresenter.getComboList(activeInfo.getHospitalId(), activeInfo.getDeptId());
        }
    }


    /**
     * 轮询查询用户支付信息,8s一次,轮询20次
     */
    Disposable disposableSlPay;

    private void selectPayInterval() {
        RxUtil.closeDisposable(disposableSlPay);

        disposableSlPay = Observable.interval(10, 8, TimeUnit.SECONDS)
                .take(20)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mPresenter.intervalSLOrder();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.d("执行完成：超时");
                        Looper.prepare();
                        showToast("支付超时");
                        Looper.loop();
//                        pop();
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoEvent event) {
        if (videoHisDao == null) return;
        switch (event.getCode()) {
            case VideoEvent.PREMIUM:
                showToast("订单已取消");
                pop();
                break;
            case VideoEvent.RELOGIN:
                switchContentWithCid(videoHisDao.getCid(), videoHisDao.getPlayType());
                break;
            case VideoEvent.FULLSCREEN:

                break;
            case VideoEvent.EXIT_FULLSCREEN:
                break;
            case VideoEvent.PAUSE:
                videoView.onPause();
                break;
            case VideoEvent.RESUME:
                RxUtil.closeDisposable(disposableSlPay);
                if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                    videoOrImageDialog.dismiss();
                }
                if (newVideoPayDialog != null && newVideoPayDialog.isShowing()) {
                    newVideoPayDialog.dismiss();
                }
                videoView.start();
                break;
        }
    }

    @Override
    public void onStop() {
        if(videoView!=null) {
            videoView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (videoView != null) {
            videoView.onDestroy();
        }
        RxUtil.closeDisposable(disposableSlPay);
        RxUtil.closeDisposable(diPayDialog);
        RxUtil.closeDisposable(disposableIsValid);
        RxUtil.closeDisposable(diVerifyPlayingStatus);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        try {
            VideoService.getInstance().addVideoCount(videoHisDao.getVideoId() + "", videoHisDao
                    .getCid(), videoHisDao.getName(), startTime, System.currentTimeMillis());
            VideoService.getInstance().addVideoInfoDb(videoHisDao.getVideoId() + "", videoHisDao
                    .getCid(), videoHisDao.getName(), startTime, System.currentTimeMillis());
            if (handler != null) {
                handler.removeMessages(SHOW_PAY_DIALOG);
                handler.removeMessages(0x01);
                handler = null;
            }
            EventBus.getDefault().unregister(this);
            if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                videoOrImageDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void getComboList(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                Gson gson = new Gson();
                PriceBean priceBean = gson.fromJson(data, PriceBean.class);
                priceList = priceBean.getData();

                showNewVideoPayDialog();
                //查询是否有微信任务
//                if (activeInfo != null)
//                {
//                    if (weiXinTaskData == null)
//                    {
//                        if (promptDialog == null)
//                        {
//                            promptDialog = new PromptDialog(getActivity());
//                        }
//                        promptDialog.showLoading("正在加载...");
//
//                        mPresenter.getWeiXinTask("" + activeInfo.getHospitalId(),"" + activeInfo.getDeptId());
//                    }else {
//                        if (promptDialog != null)
//                        {
//                            promptDialog.dismiss();
//                        }
//
//
//                    }
//                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getComboListFail() {
        promptDialog.dismiss();
    }

    /**
     * 获取到了微信任务
     * @param data
     */
    @Override
    public void getWeiXinTask(WeiXinTask.WeiXinTaskData data) {
        //显示支付界面
        promptDialog.dismiss();
        weiXinTaskData = data;
        showNewVideoPayDialog();

    }

    @Override
    public void getWeiXinTaskFail() {
        promptDialog.dismiss();
        showNewVideoPayDialog();
    }


    //显示支付界面
    private void showNewVideoPayDialog()
    {
        newVideoPayDialog = new NewVideoPayDialog(getActivity(), R.style
                .DialogFullscreen, weiXinTaskData, priceList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_close:
                        pop();
                        isShowDialog = false;
                        newVideoPayDialog.dismiss();
                        break;
                    case R.id.btn_code:
                        newVideoPayDialog.dismiss();
                        showPassDialog();
                        break;
                }
            }
        }, new NewVideoPayDialog.IWeiXinTaskListener() {
            @Override
            public void onSuccess() {
                LogUtil.d("任务已经完成");
                FancyToast.makeText(LauncherApplication.getContext(),"任务已经完成",Toast.LENGTH_SHORT).show();
                isPaySuccess = true;
                newVideoPayDialog.dismiss();
                videoView.start();
                weiXinTaskData = null;

            }

            @Override
            public void onFail() {
                pop();
                FancyToast.makeText(LauncherApplication.getContext(),"任务完成失败",Toast.LENGTH_SHORT).show();
                LogUtil.d("任务完成失败");
                newVideoPayDialog.dismiss();
                isPaySuccess = false;
            }
        });
        newVideoPayDialog.setCanceledOnTouchOutside(false);
        newVideoPayDialog.show();
    }

    @Override
    public void verfycode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                showToast("验证码验证成功");
                popEnterPassword.dismiss();
                isPaySuccess = true;
                videoView.start();
                try {
                    //每次验证成功后就更新一下广告
                    AdvertsUtil.getInstance().queryAdverts(UIUtils.fun(AdvertsTopics.CODE_HOME_LB,
                            AdvertsTopics.CODE_HOME_DIALOG, AdvertsTopics.CODE_LOCK,
                            AdvertsTopics.CODE_PUBLIC, AdvertsTopics.CODE_VERTICAL,
                            AdvertsTopics.CODE_VIDEO_CORNER, AdvertsTopics.CODE_VIDEO_DIALOG,
                            AdvertsTopics.CODE_RW), null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                isPaySuccess = false;
                showToast("验证码验证失败");
            }
            if (popEnterPassword.isShowing())
                popEnterPassword.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verfycodeError() {
        showToast("网络错误,请重试");
    }

    @Override
    public void setPayPackageList(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String objdata = jsonObject.optString("data");
            JSONObject obj = new JSONObject(objdata);
            int status = obj.optInt("status");
            if(status==0){
                String secondData = obj.optString("data");
                JSONObject secondObj = new JSONObject(secondData);
                if (secondObj.optInt("status") == 2) {
                    String expire_time = secondObj.optString("expire_time");
                    boolean isValid = DateUtil.isValid(expire_time);
                    if (isValid) {
                        LitePalDb.setZkysDb();
                        PayInfoDao payInfoDao=new PayInfoDao();
                        payInfoDao.setExpireTime(expire_time);
                        DbHelper.insertToVipData(LitePalDb.DBNAME_ZKYS,payInfoDao);
                    }
                }
                if (secondObj.optInt("status") != 2 && secondObj.optInt("status") != 3) {

                        //用户已经登录微信，直接显示支付页面
                        showPayDialog();
//                    if (UserUtil.getUserBean() != null)
//                    {
//                        //用户已经登录微信，直接显示支付页面
//                        showPayDialog();
//                    }else {
//                        loginWeixin();
//                    }



                }
            }else {
//                if (UserUtil.getUserBean() != null)
//                {
//                    //用户已经登录微信，直接显示支付页面
                    showPayDialog();
//                }else {
//                    loginWeixin();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 登录微信
     */
    private void loginWeixin(){
        //暂停播放
        if (videoView != null) {
            videoView.pause();
        }

        if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
            videoOrImageDialog.dismiss();
        }
        FancyToast.makeText(LauncherApplication.getContext(),"请先登录微信", Toast.LENGTH_LONG).show();

        loginDialog = new LoginDialog(getActivity(), R.style
                .DialogFullscreen, new LoginDialog.OnLoginListener() {
            @Override
            public void onSuccess() {
                LogUtil.d("微信登录成功");
                loginDialog.dismiss();
                FancyToast.makeText(LauncherApplication.getContext(),"微信登录成功！",Toast.LENGTH_LONG).show();
                showPayDialog();
                if (promptDialog == null)
                {
                    promptDialog = new PromptDialog(getActivity());
                }
                promptDialog.showLoading("正在加载...");

            }

            @Override
            public void onFail() {
                LogUtil.d("微信登录失败");
                FancyToast.makeText(LauncherApplication.getContext(),"微信登录失败！",Toast.LENGTH_LONG).show();
                pop();
            }
        });

        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.show();

    }

    @Override
    public void setPayFail() {
        checkIsValid();
    }

    @Override
    public void intervalSLOrder(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String objdata = jsonObject.optString("data");
            JSONObject obj = new JSONObject(objdata);
            int status = obj.optInt("status");
            //status 1 没有租用
            //第二层data 1等待支付 2正在租用 3正在逾期 4已完成 5已取消
            if (status == 0) { //租用状态看看是否已过期
                String secondData = obj.optString("data");
                JSONObject secondObj = new JSONObject(secondData);
                if (secondObj.optInt("status") == 2) {
                    String expire_time = secondObj.optString("expire_time");
                    boolean isValid = DateUtil.isValid(expire_time);
//                    if (isValid && !PayUtils.isValid(PayEntity.ORDER_TYPE_VIDEO)) {
                    if (isValid ) {
//                        PayEntity payEntity = new PayEntity(PayEntity.ORDER_TYPE_VIDEO, expire_time);
//                        PayUtils.setPaied(payEntity);
                        LitePalDb.setZkysDb();
                        PayInfoDao payInfoDao=new PayInfoDao();
                        payInfoDao.setExpireTime(expire_time);
                        DbHelper.insertToVipData(LitePalDb.DBNAME_ZKYS,payInfoDao);

                        EventBus.getDefault().post(new VideoEvent(VideoEvent.RESUME));
                        EventBus.getDefault().post(new PayEvent(PayEntity.ORDER_TYPE_VIDEO));
                        RxUtil.closeDisposable(disposableSlPay);
                        if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                            videoOrImageDialog.dismiss();
                        }
                        if (newVideoPayDialog != null && newVideoPayDialog.isShowing()) {
                            newVideoPayDialog.dismiss();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //输入验证码框
    private void showPassDialog() {
        popEnterPassword = new PopEnterPassword(getActivity());
        // 显示窗口
        popEnterPassword.showAtLocation(getActivity().findViewById(R.id.rel_video_play),
                Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
        popEnterPassword.setOutsideTouchable(false);
        popEnterPassword.setFocusable(false);
        popEnterPassword.setTouchable(true);
        popEnterPassword.setOnPassFinish(new OnPasswordFinish() {
            @Override
            public void passwordFinish(String password) {
                mPresenter.verfycode(password);
            }
        });
        popEnterPassword.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isPaySuccess) {
                    newVideoPayDialog.show();
                }
            }
        });
    }
}
