package com.muju.note.launcher.app.video.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.devbrackets.android.api.video.impl.VideoErrorInfo;
import com.devbrackets.android.component.utils.ViewScaleUtil;
import com.devbrackets.android.media.listener.OnVideoPreparedListener;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.video.adapter.VideoLineAdapter;
import com.muju.note.launcher.app.video.contract.VideoLineContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.dialog.VideoOrImageDialog;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.app.video.event.VideoPauseEvent;
import com.muju.note.launcher.app.video.presenter.VideoLinePresenter;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.app.video.util.wotv.ExpandVideoView2;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.adverts.AdvertsUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.unicom.common.VideoSdkConfig;
import com.unicom.common.base.video.IVideoEvent;
import com.unicom.common.base.video.expand.ExpandVideoListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.droidsonroids.gif.GifImageView;

/**
 * 直播TV
 */
public class WoTvVideoLineFragment extends BaseFragment<VideoLinePresenter> implements View.OnClickListener, VideoLineContract.View {

    private static final String TAG = "WoTvVideoLineFragment";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.rv_video_view)
    RecyclerView rvVideoView;
    @BindView(R.id.iv_loading)
    GifImageView ivLoading;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_error)
    LinearLayout llError;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.video_view)
    ExpandVideoView2 videoView;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.iv_line_back)
    ImageView ivLineBack;
    private boolean isShowDialog = true;
    private List<VideoInfoDao> videoInfoDaos;
    private VideoLineAdapter lineAdapter;
    private VideoOrImageDialog videoOrImageDialog;
    private VideoInfoDao infoDao;

    @Override
    public int getLayout() {
        return R.layout.fragment_wotv_line;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);
        EventBus.getDefault().register(this);
        videoInfoDaos = new ArrayList<>();
        lineAdapter = new VideoLineAdapter(R.layout.rv_item_video_line, videoInfoDaos);
        rvVideoView.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(), LinearLayoutManager.VERTICAL, false));
        rvVideoView.setAdapter(lineAdapter);

        mPresenter.queryVideo();


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

        // 设置播放器回调
        setVideoView();

        lineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                lineAdapter.setPos(position);
                llLoading.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                infoDao = videoInfoDaos.get(position);
                videoView.pause();
                playVideoAndSetUI();
                lineAdapter.notifyDataSetChanged();
            }
        });

        ivLineBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewScaleUtil.widthFixed(videoView, 1080, 737);
                ivLineBack.setVisibility(View.GONE);
            }
        });

    }



    @Override
    public void initPresenter() {
        mPresenter = new VideoLinePresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
            case R.id.tv_null:
                isShowDialog = false;
                pop();
                break;
        }
    }

    @Override
    public void getVideoSuccess(List<VideoInfoDao> list) {
        videoInfoDaos.addAll(list);
        infoDao = videoInfoDaos.get(0);
        lineAdapter.notifyDataSetChanged();
        playVideoAndSetUI();
        llContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoNull() {
        llContent.setVisibility(View.GONE);
        llNull.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        llLoading.setVisibility(View.GONE);
    }

    //监听视频播放暂停
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoPauseEvent event) {
        LogFactory.l().i("VideoPauseEvent==" + event.isPause);
        if (event.isPause && isShowDialog) {
            AdvertsCodeDao codeDao = LitePal.where("code =?", AdvertsTopics.CODE_VIDEO_DIALOG).findFirst(AdvertsCodeDao.class);
            try {
                if (videoOrImageDialog == null) {
                    videoOrImageDialog = new VideoOrImageDialog(getActivity(), R.style.dialog);
                    if (codeDao != null) {
                        AdvertsUtil.getInstance().showVideoDialog(codeDao, videoOrImageDialog);
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
        EventBus.getDefault().post(new VideoNoLockEvent(true));
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
                    if (!videoView.isPlaying()) {
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
                LogUtil.i(TAG, "changeScreenOrientation:" + isFullScreen);
                if (isFullScreen) {
                    ViewScaleUtil.widthFixed(videoView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    ivLineBack.setVisibility(View.VISIBLE);
                } else {
                    ViewScaleUtil.widthFixed(videoView, 1080, 737);
                    ivLineBack.setVisibility(View.GONE);
                }
            }

            @Override
            public void onControlsVisiable(boolean isShown) {
                //TODO 播放器默认的控制层是否展示状态，如果是无UI模式，则不需要理会此通知
                LogUtil.d("VideoPlayer:isShown %s", isShown);
            }

            @Override
            public boolean onVideoComplete() {
                //TODO 视频播放完成（一个视频达到duration的末尾），通知业务层
                showToast("直播已播放完成");
                pop();
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
                    llError.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);
                    RxUtil.closeDisposable(diVerifyPlayingStatus);
                    if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                        videoOrImageDialog.dismiss();
                    }
                    switch (e.getCode()) {
                        case VideoErrorInfo.CODE_ACCOUNT_CHECK_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_CONTENTE_PERMISSION:
                            if (VideoSdkConfig.getInstance().getUser().isLogined()) {
                                //这里发生错误就先重新登录
                                showToast("没有权限播放此视频");
                            } else {
                                WoTvUtil.getInstance().login();
                                showToast("网络环境异常，请检查！");
                            }
                            break;
                        case VideoErrorInfo.CODE_VIDEO_INNER_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_GET_ERROR:
                            showToast(e.getMessage() + "");
                            break;
                        case VideoErrorInfo.CODE_ORDER_CHECK_ERROR:
                        case VideoErrorInfo.CODE_VIDEO_URL_ERROR:
                            WoTvUtil.getInstance().login();
                            showToast(e.getMessage() + "");
                            break;
                    }
                } catch (Exception es) {
                    es.printStackTrace();
                }

            }

            @Override
            public void playHalfHour() {

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
                            if (playedDuration == 0 && currentPosition == 0 && !videoView.isPlaying()) {
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
        switchContentWithCid(infoDao.getCid(), infoDao.getVideoType() + "");
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
                                infoDao.getName(), mControlListener, 2);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        videoView.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            if (videoView != null) {
                videoView.onDestroy();
            }
            if (videoOrImageDialog != null && videoOrImageDialog.isShowing()) {
                videoOrImageDialog.dismiss();
            }
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
