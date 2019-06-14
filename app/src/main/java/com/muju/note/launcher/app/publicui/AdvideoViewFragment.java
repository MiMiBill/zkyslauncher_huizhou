package com.muju.note.launcher.app.publicui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.publicAdress.contract.PublicContract;
import com.muju.note.launcher.app.publicAdress.presenter.PublicPresenter;
import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.VideoPlayControlView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 广告看视频
 */
public class AdvideoViewFragment extends BaseFragment<PublicPresenter> implements PublicContract.View {
    private static String ADVIDEO_URL_FLAG = "advideo";
    private static String ADVIDEO_ID = "advideoId";
    private static String ADVIDEO_TYPE = "advideo_type";
    private static String ADVIDEO_SECOND = "advideo_second";
    @BindView(R.id.vv_videoView)
    VideoPlayControlView mVvVideoView;
    private int advertId;
    private long startTime;
    private String videoUrl="";
    private int type=0;  //0默认是普通广告,1是任务广告
    private int second=0;
    private boolean isTask=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(isTask)
                    mPresenter.doTask(UserUtil.getUserBean().getId(),advertId);
                    break;
            }
        }
    };
    public static AdvideoViewFragment newInstance(int id, String url,int type,int second) {
        Bundle args = new Bundle();
        args.putString(ADVIDEO_URL_FLAG, url);
        args.putInt(ADVIDEO_ID, id);
        args.putInt(ADVIDEO_TYPE, type);
        args.putInt(ADVIDEO_SECOND, second);
        AdvideoViewFragment fragment = new AdvideoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_ad_video_view;
    }

    @Override
    public void initData() {
        startTime = System.currentTimeMillis();
        advertId = getArguments().getInt(ADVIDEO_ID, 0);
        videoUrl = getArguments().getString(ADVIDEO_URL_FLAG);
        type = getArguments().getInt(ADVIDEO_TYPE,0);
        second=getArguments().getInt(ADVIDEO_SECOND,0);
        if (videoUrl != null && !videoUrl.equals("")) {
            mVvVideoView.getVideoView().setVideoPath(videoUrl);
            mVvVideoView.getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVvVideoView.getVideoView().start();
                }
            });
        }
        if(type==1){
            isTask=true;
            handler.sendEmptyMessageDelayed(1,1000*second); //暂定看second秒广告相当于做任务
        }
        mVvVideoView.setBackListener(new VideoPlayControlView.BackListener() {
            @Override
            public void back() {
                doFinish();
            }
        });
    }


    private void doFinish() {
        long currentTime=System.currentTimeMillis();
        NewAdvertsUtil.getInstance().addData(advertId,NewAdvertsUtil.TAG_BROWSETIME,currentTime-startTime);
        NewAdvertsUtil.getInstance().addDataInfo(advertId,NewAdvertsUtil.TAG_BROWSETIME,startTime,currentTime);
        if (mVvVideoView != null) {
            mVvVideoView.onDestroy();
        }
        pop();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        EventBus.getDefault().post(new VideoNoLockEvent(false));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        EventBus.getDefault().post(new VideoNoLockEvent(true));
        if(isTask)
        handler.removeMessages(1);
    }

    @Override
    public void initPresenter() {
        mPresenter=new PublicPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void verfycode(String response) {

    }

    @Override
    public void doTask(TaskBean taskBean) {
        if(taskBean!=null) {
            if (taskBean.getAdverts() != null) {
                SPUtil.saveDataList(Constants.AD_TASK_LIST, taskBean.getAdverts());
            }
        }
    }
}
