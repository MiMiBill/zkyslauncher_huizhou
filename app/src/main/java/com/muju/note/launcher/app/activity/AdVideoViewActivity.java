package com.muju.note.launcher.app.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.view.VideoPlayControlView;

import butterknife.BindView;

public class AdVideoViewActivity extends BaseActivity {
    private static String VIDEO_URL_FLAG = "video";
    private static String VIDEO_ID = "videoId";
    @BindView(R.id.vv_videoView)
    VideoPlayControlView mVvVideoView;
    private int advertId;
    private long startTime;

    public static void launch(Context context, String url, int id) {
        Intent intent = new Intent(context, AdVideoViewActivity.class);
        intent.putExtra(VIDEO_URL_FLAG, url);
        intent.putExtra(VIDEO_ID, id);
        context.startActivity(intent);
    }


    @Override
    public void initData() {
        hideActionBar();
        startTime = System.currentTimeMillis();
        advertId = getIntent().getIntExtra(VIDEO_ID, 0);
        if (getIntent().getStringExtra(VIDEO_URL_FLAG) != null) {
            mVvVideoView.getVideoView().setVideoPath(getIntent().getStringExtra(VIDEO_URL_FLAG));
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
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        long currentTime=System.currentTimeMillis();
        NewAdvertsUtil.getInstance().addData(advertId,NewAdvertsUtil.TAG_BROWSETIME,currentTime-startTime);
        NewAdvertsUtil.getInstance().addDataInfo(advertId,NewAdvertsUtil.TAG_BROWSETIME,startTime,currentTime);
        if (mVvVideoView != null) {
            mVvVideoView.onDestroy();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_ad_video_view;
    }

}
