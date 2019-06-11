package com.muju.note.launcher.app.publicui;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.view.VideoPlayControlView;

import butterknife.BindView;

/**
 * 广告看视频
 */
public class AdvideoViewFragment extends BaseFragment {
    private static String VIDEO_URL_FLAG = "video";
    private static String VIDEO_ID = "videoId";
    @BindView(R.id.vv_videoView)
    VideoPlayControlView mVvVideoView;
    private int advertId;
    private long startTime;
    private String videoUrl="";

    public static AdvideoViewFragment newInstance(int id, String url) {
        Bundle args = new Bundle();
        args.putString(VIDEO_URL_FLAG, url);
        args.putInt(VIDEO_ID, id);
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
        advertId = getArguments().getInt(VIDEO_ID, 0);
        videoUrl = getArguments().getString(VIDEO_URL_FLAG);
        if (videoUrl != null && !videoUrl.equals("")) {
            LogFactory.l().i("url==="+videoUrl);
            mVvVideoView.getVideoView().setVideoPath(videoUrl);
            mVvVideoView.getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVvVideoView.getVideoView().start();
                }
            });
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
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }
}
