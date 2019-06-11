package com.muju.note.launcher.app.setting.ui;

import android.content.ContentResolver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.light.RectProgress;

import butterknife.BindView;
import butterknife.Unbinder;

public class VoiceFragment extends BaseFragment {
    @BindView(R.id.rectProgress_light)
    RectProgress rectProgressLight;
    @BindView(R.id.rectProgress_voice)
    RectProgress rectProgressVoice;
    Unbinder unbinder;
    private boolean isRelease = true; //判断MediaPlayer是否释放的标志
    private MediaPlayer mediaPlayer = null;
    private long maxVoice;

    @Override
    public int getLayout() {
        return R.layout.fragment_voice;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rectProgressLight.setMax(255);
        maxVoice = SPUtil.getLong(SpTopics.PAD_CONFIG_VOLUME_RATE);
        if (maxVoice >= 0) {
            rectProgressVoice.setMax((int)(maxVoice/100D*15));
        } else {
            rectProgressVoice.setMax(SystemUtils.getMaxVolume(LauncherApplication.getContext()));
        }
        int currentVolume = SystemUtils.getCurrentVolume(LauncherApplication.getContext());
        LogFactory.l().i("currentVolume==="+currentVolume);
        LogFactory.l().i("maxVoice==="+maxVoice);
        rectProgressVoice.setProgress(currentVolume);
        rectProgressLight.setProgress(SystemUtils.getScreenBrightness());
        rectProgressLight.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                saveBrightness(getContext().getContentResolver(), currentValue);
            }
        });

        rectProgressVoice.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
//                LogFactory.l().i("percent===" + percent);
//                LogFactory.l().i("currentValue===" + SystemUtils.getCurrentVolume(getContext()));
                if (currentValue != SystemUtils.getCurrentVolume(getContext())) {
                    int rateValue = 0;
                    if (maxVoice >= 0) {
                        rateValue = percent;
                    } else {
                        rateValue = (int) (currentValue / 1.0 / SystemUtils.getMaxVolume(getContext()) * 100);
                    }
//                    LogFactory.l().i("currentValue===" + currentValue);
//                    LogFactory.l().i("rateValue===" + rateValue);
                    SystemUtils.setVolume(getContext(), rateValue);
                }
            }
        });

        rectProgressVoice.setOnActionUpListener(new RectProgress.OnActionUpListener() {
            @Override
            public void onActionUp() {
                if (isRelease) {
                    //在raw下的资源
                    mediaPlayer = MediaPlayer.create(LauncherApplication.getContext(), R.raw.messagetips);
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


    }


    public static void saveBrightness(ContentResolver resolver, int brightness) {
        //改变系统的亮度值
        //设置为手动调节模式
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //保存到系统中
        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
                brightness);
        resolver.notifyChange(uri, null);
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
