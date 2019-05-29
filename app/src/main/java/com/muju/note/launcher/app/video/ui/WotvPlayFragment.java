package com.muju.note.launcher.app.video.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.api.video.impl.VideoErrorInfo;
import com.devbrackets.android.media.listener.OnVideoPreparedListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.publicAdress.ui.PublicActivity;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.app.video.dialog.VideoOrImageDialog;
import com.muju.note.launcher.app.video.dialog.VideoPayDialog;
import com.muju.note.launcher.app.video.event.VideoCodeFailEvent;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.event.VideoPauseEvent;
import com.muju.note.launcher.app.video.event.VideoReStartEvent;
import com.muju.note.launcher.app.video.event.VideoStartEvent;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.app.video.util.PayUtils;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.app.video.util.wotv.ExpandVideoView2;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.unicom.common.VideoSdkConfig;
import com.unicom.common.base.video.IVideoEvent;
import com.unicom.common.base.video.expand.ExpandVideoListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import pl.droidsonroids.gif.GifImageView;

public class WotvPlayFragment extends BaseFragment implements View.OnClickListener {

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
    Unbinder unbinder;
    private boolean isCodeFail = false;
    private static final String TAG = "WotvPlayFragment";

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
    public static final String VIDEO_TYPE_EPISODE = "2";
    public static final String VIDEO_TYPE_VARIETY = "4";
    private VideoOrImageDialog videoOrImageDialog;
    // 当前播放的集数
    private static int EPISODE_POSITION;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    private VideoHisDao videoHisDao;

    private Disposable diPayDialog;

    private long startTime;
    private LoginDialog loginDialog;

    public void setHisDao(VideoHisDao videoHisDao) {
        this.videoHisDao = videoHisDao;
    }

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
                                    setPayPackageList();
                                }
                            });
                }
                if (msg.what == 0x01) {
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
        EventBus.getDefault().post(new VideoNoLockEvent(false));
        try {

            startTime = System.currentTimeMillis();

            EventBus.getDefault().register(this);

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
                    videoView.setBasicControlDialogsVisible(true, true);
                    llLoading.setVisibility(View.GONE);
                }
            });

            // 添加历史记录
            videoHisDao.setCreateTime(System.currentTimeMillis() + "");
            VideoService.getInstance().addVideoHisInfo(videoHisDao);
        } catch (Exception e) {
            tvTitle.setText(videoHisDao.getName());

        }

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;
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
                verifyPlayingStatus();
                //TODO 播放器的默认海报展示的时候，会通知业务层，由业务层处理业务逻辑。
                Log.e(TAG, "海报是否显示：" + isVisiable);
                if (!videoView.isPlaying()) {
                    llLoading.setVisibility(View.VISIBLE);
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
                //TODO 视频播放完成（一个视频达到duration的末尾），通知业务层
                LogUtil.e(TAG, "onVideoComplete:");
                switch (videoHisDao.getPlayType()) {
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
            }

            @Override
            public void onCollectedVideo(boolean isCollected) {
                //TODO 视频是否被收藏，配套沃视频的功能，可不实现，不影响
            }

            @Override
            public void onError(VideoErrorInfo.VideoException e) {
                LogUtil.e(TAG, "onError:" + e.toString());
                //TODO VideoErrorInfo配套查阅错误代码
                LogUtil.e(TAG, "错误代码：" + e.getCode());
                LogUtil.e(TAG, "错误信息：" + e.getMessage());
                switch (e.getCode()) {
                    case VideoErrorInfo.CODE_ACCOUNT_CHECK_ERROR:
                    case VideoErrorInfo.CODE_VIDEO_CONTENTE_PERMISSION:
                        if (VideoSdkConfig.getInstance().getUser().isLogined()) {
                            //这里发生错误就先重新登录
                            showToast("没有权限播放此视频");
                            pop();
                        } else {
                            WoTvUtil.getInstance().login();
                            showToast("网络环境异常，请检查！");
                        }
                        break;
                    case VideoErrorInfo.CODE_VIDEO_INNER_ERROR:
                    case VideoErrorInfo.CODE_VIDEO_GET_ERROR:
                        showToast(e.getMessage() + "");
                        pop();
                        break;
                    case VideoErrorInfo.CODE_ORDER_CHECK_ERROR:
                    case VideoErrorInfo.CODE_VIDEO_URL_ERROR:
                        WoTvUtil.getInstance().login();
                        showToast(e.getMessage() + "");
                        pop();
                        break;
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
                                videoHisDao.getName(), mControlListener);
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
            List<AdvertsBean> adverts = CacheUtil.getDataList(AdvertsTopics.CODE_VIDEO_CORNER);
            if (adverts != null && adverts.size() > 0) {
                NewAdvertsUtil.getInstance().showByImageView(getActivity(), adverts,
                        ivCorner, ivColse, relCornor);
            }
        }
    }


    @Override
    public void onStart () {
        super.onStart();
        if (videoView != null && (!isCodeFail)) {
//            LogFactory.l().e("onStart");
            videoView.onStart();
        }
    }

    @Override
    public void onPause () {
        super.onPause();
        if (videoView != null) {
//            LogFactory.l().e("onPause");
            videoView.onPause();
        }
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
            payDialog.dismiss();
            loginDialog.dismiss();
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
        if (event.isPause) {
            List<AdvertsBean> adverts = CacheUtil.getDataList(AdvertsTopics.CODE_VIDEO_DIALOG);
            try {
                if (videoOrImageDialog == null) {
                    videoOrImageDialog = new VideoOrImageDialog(getActivity(), R.style.dialog);
                    if (adverts != null && adverts.size() > 0)
                        NewAdvertsUtil.getInstance().showVideoDialog(adverts, videoOrImageDialog);
                } else {
                    if(adverts.get(0).getCloseType()==2){
                        videoOrImageDialog.closeBySelf(adverts.get(0).getSecond());
                    }
                    videoOrImageDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 检查是否需要支付
     */
    private Disposable disposableIsValid;
    private long pay_cuntDown = 60 * 1000 * 6;//倒计时收费
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
                            if (videoView.getPlayedDuration() != 0 && videoView
                                    .getCurrentPosition() != 0) {
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
     * 查询支付信息
     */
    private void setPayPackageList() {
        Map<String, String> params = new HashMap();
        String iccid = MobileInfoUtil.getIMEI(getContext());
        params.put("code", iccid);
        OkGo.<String>post(UrlUtil.getGetDeviceStatus())
                .tag(this)
                .params("code", iccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtil.d("body:%s", body);
                        try {
                            JSONObject jsonObject = new JSONObject(body).getJSONObject("data");

                            int status = jsonObject.optInt("status");
                            int isConvention = jsonObject.optInt("isConvention");
                            //status 1 没有租用
                            /*
                            * if (status == 1 || contentStatus != 2 || contentStatus != 3) {
                                showPayDialog(isConvention);
                            }
                            * */
                            if (status == 1 || (jsonObject.getJSONObject("data").optInt
                                    ("status")
                                    != 2 && jsonObject.getJSONObject("data").optInt("status") !=
                                    3)) {
                                showPayDialog(isConvention);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());

                    }
                });
    }

    /**
     * 展示支付信息
     *
     * @param isConvention
     */
    private VideoPayDialog payDialog;

    private void showPayDialog(int isConvention) {
        //暂停播放
        videoView.pause();
        if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
            videoOrImageDialog.dismiss();
        }
        //开启轮询
        selectPayInterval();
        payDialog = new VideoPayDialog(getActivity(), R.style.DialogFullscreen, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivClose:
                    case R.id.ivClose2:
                        pop();
                        payDialog.dismiss();
                        break;

                    case R.id.tvHelp:
                        payDialog.dismiss();
                        startForResult(new VideoHelperFragment(), 1001);
                        break;

                    case R.id.btPay:
                        payDialog.setPay();
                        payDialog.setQrde();
                        break;
                    case R.id.tv_login:
                        showLoginDialog();
                        break;
                }
            }
        });
        payDialog.setCanceledOnTouchOutside(false);
        payDialog.show();
    }


    //登录
    private void showLoginDialog() {
        loginDialog = new LoginDialog(getActivity(), R.style.dialog, new
                LoginDialog.OnLoginListener() {
                    @Override
                    public void onSuccess() {
                        LogFactory.l().e("onSuccess");
                        goToPublicActivity();
                        loginDialog.dismiss();
                    }

                    @Override
                    public void onFail() {
                        LogFactory.l().e("onFail");
                        showToast("登录失败");
                    }
                });
        loginDialog.show();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            payDialog.show();
        }
    }

    private void goToPublicActivity () {
        Intent intent = new Intent(getActivity(), PublicActivity.class);
        startActivity(intent);
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
                        intervalSLOrder();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.d("执行完成：超时");
                        showToast("支付超时");
//                        pop();
                    }
                });
    }

    /**
     * 轮播查询订单
     */
    private void intervalSLOrder() {
        Map<String, String> params = new HashMap();
        String iccid = MobileInfoUtil.getIMEI(getContext());
        params.put("code", iccid);
        OkGo.<String>post(UrlUtil.getGetDeviceStatus())
                .tag(this)
                .params("code", iccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtil.d("body:%s", body);
                        try {
                            JSONObject jsonObject = new JSONObject(body).getJSONObject("data");
                            int status = jsonObject.optInt("status");
                            //status 1 没有租用
                            //第二层data 1等待支付 2正在租用 3正在逾期 4已完成 5已取消
                            if (status == 0) { //租用状态看看是否已过期
                                if (jsonObject.getJSONObject("data").getInt("status") == 2) {
                                    String expire_time = jsonObject.getJSONObject("data")
                                            .optString("expire_time");
                                    boolean isValid = DateUtil.isValid
                                            (expire_time);
                                    if (isValid && !PayUtils.isValid(PayEntity
                                            .ORDER_TYPE_VIDEO)) {
                                        PayEntity payEntity = new PayEntity(PayEntity
                                                .ORDER_TYPE_VIDEO, expire_time);
                                        PayUtils.setPaied(payEntity);
                                        EventBus.getDefault().post(new VideoEvent(VideoEvent
                                                .RESUME));
                                        EventBus.getDefault().post(new PayEvent(PayEntity
                                                .ORDER_TYPE_VIDEO));
                                        RxUtil.closeDisposable(disposableSlPay);
                                        if (videoOrImageDialog != null && videoOrImageDialog
                                                .isShowing()) {
                                            videoOrImageDialog.dismiss();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtil.d(response.body());

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
                videoView.start();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        videoView.onStop();
    }

    @Override
    public void onDestroy() {
        try {
//            videoHisDao.setDuration(videoView.getDuration());\
            VideoService.getInstance().addVideoCount(videoHisDao.getVideoId() + "", videoHisDao.getName(), startTime, System.currentTimeMillis());
            VideoService.getInstance().addVideoInfoDb(videoHisDao.getVideoId() + "", videoHisDao.getName(), startTime, System.currentTimeMillis());
            VideoService.getInstance().addVideoCount(videoHisDao.getVideoId() + "", videoHisDao
                    .getName(), startTime, System.currentTimeMillis());
            VideoService.getInstance().addVideoInfoDb(videoHisDao.getVideoId() + "", videoHisDao
                    .getName(), startTime, System.currentTimeMillis());
            if (videoView != null) {
                videoView.pause();
                videoView.onDestroy();
                videoView = null;
            }
            RxUtil.closeDisposable(disposableSlPay);
            RxUtil.closeDisposable(diPayDialog);
            RxUtil.closeDisposable(disposableIsValid);
            if (handler != null) {
                handler.removeMessages(SHOW_PAY_DIALOG);
                handler.removeMessages(0x01);
                handler = null;
            }
            EventBus.getDefault().unregister(this);
            if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                videoOrImageDialog.dismiss();
            }
            EventBus.getDefault().post(new VideoNoLockEvent(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
