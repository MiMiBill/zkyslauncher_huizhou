package com.muju.note.launcher.app.setting.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.setting.event.VolumeEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.BrightnessUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.light.RectProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class VoiceFragment extends BaseFragment {

    public static final String STATUS = "status";
    @BindView(R.id.rectProgress_light)
    RectProgress rectProgressLight;
    @BindView(R.id.rectProgress_voice)
    RectProgress rectProgressVoice;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_bg)
    View view_bg;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;


    private boolean isRelease = true; //判断MediaPlayer是否释放的标志
    private MediaPlayer mediaPlayer = null;
    private long maxVoice = 0;
    private int voicePercent = -1;

    public static VoiceFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt(STATUS, status);
        VoiceFragment fragment = new VoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_voice;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        rectProgressLight.setMax(255);
        maxVoice = SPUtil.getLong(SpTopics.PAD_CONFIG_VOLUME_RATE);

//        rectProgressVoice.setMax(SystemUtils.getMaxVolume(LauncherApplication.getContext()));
        rectProgressVoice.setMax(100);
        int currentVolume = (int )(SystemUtils.getCurrentVolume(LauncherApplication.getContext()) * 100.0 / SystemUtils.getMaxVolume(LauncherApplication.getContext())) ;
        int screenBrightness = BrightnessUtils.getScreenBrightness(getActivity());
        LogUtil.d("屏幕亮度：" + screenBrightness);
        rectProgressVoice.setProgress(currentVolume);
        rectProgressLight.setProgress(screenBrightness);
        rectProgressLight.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                BrightnessUtils.setBrightness(getActivity(), currentValue);
                BrightnessUtils.saveBrightness(getActivity(), currentValue);
            }
        });

        rectProgressVoice.setChangedListener(onVoiceChangeListener);
        rectProgressVoice.setOnActionUpListener(new RectProgress.OnActionUpListener() {
            @Override
            public void onActionUp() {
//                int currentVolume = SystemUtils.getCurrentVolume(getContext());
//                LogUtil.d("currentVolume:" + currentVolume);
//                rectProgressVoice.setProgress(currentVolume);
                LogUtil.d("voicePercent:" + voicePercent);
                SystemUtils.setVolume(getContext(), voicePercent);
                rectProgressVoice.setProgress(voicePercent);
                if (isRelease) {
                    //在raw下的资源
                    mediaPlayer = MediaPlayer.create(LauncherApplication.getContext(), R.raw
                            .messagetips);
                    isRelease = false;
                }
                mediaPlayer.start(); //开始播放

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        isRelease = true;
                    }
                });
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        if (getArguments().getInt(STATUS) == 1) {
            relTitlebar.setVisibility(View.VISIBLE);
            tvTitle.setText("系统设置");
            view_bg.setVisibility(View.VISIBLE);
        } else {
            relTitlebar.setVisibility(View.GONE);
        }
    }


    RectProgress.OnProgressChangedListener onVoiceChangeListener = new RectProgress
            .OnProgressChangedListener() {
        @Override
        public void onProgressChanged(int currentValue, int percent) {
            if (percent > maxVoice && maxVoice > 0) {
                percent = (int) maxVoice;
            }
            voicePercent = currentValue;
            LogUtil.d("voicePercent:" + voicePercent + " currentValue:" + currentValue);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VolumeEvent volumeEvent) {
        rectProgressVoice.setChangedListener(null);
        int currentVolume = SystemUtils.getCurrentVolume(getContext());
//        LogFactory.l().i("VolumeEvent==="+currentVolume);
        rectProgressVoice.setProgress(currentVolume);
        rectProgressVoice.setChangedListener(onVoiceChangeListener);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

}
