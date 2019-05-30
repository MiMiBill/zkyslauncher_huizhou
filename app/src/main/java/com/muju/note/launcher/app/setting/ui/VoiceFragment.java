package com.muju.note.launcher.app.setting.ui;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.light.RectProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VoiceFragment extends BaseFragment {
    @BindView(R.id.rectProgress_light)
    RectProgress rectProgressLight;
    @BindView(R.id.rectProgress_voice)
    RectProgress rectProgressVoice;
    Unbinder unbinder;

    @Override
    public int getLayout() {
        return R.layout.fragment_voice;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rectProgressLight.setProgress(SystemUtils.getScreenBrightness());
        rectProgressVoice.setProgress(SystemUtils.getCurrentVolume(getContext()));

        rectProgressLight.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                LogFactory.l().i( "==onProgressChanged: " + currentValue);
                LogFactory.l().i(  "==percent: " + percent);
                saveBrightness(getContext().getContentResolver(), currentValue);
            }
        });

        rectProgressVoice.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                LogFactory.l().i( "==onProgressChanged: " + currentValue);
                LogFactory.l().i(  "==percent: " + percent);
                int rateValue = (int) (currentValue / 1.0 / SystemUtils.getMaxVolume(getContext())*100);
                if (currentValue != SystemUtils.getCurrentVolume(getContext())) {
                    SystemUtils.setVolume(getContext(), rateValue);
                }
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
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        resolver.notifyChange(uri, null);
    }

    @Override
    public void initData() {
       /* rectProgressLight.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                Log.e("zkpad","onProgressChanged==="+currentValue);
                Log.e("zkpad","percent==="+percent);
                LogFactory.l().i( "==onProgressChanged: " + currentValue);
                LogFactory.l().i(  "==percent: " + percent);
            }
        });

        rectProgressVoice.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                LogFactory.l().i( "==onProgressChanged: " + currentValue);
                LogFactory.l().i(  "==percent: " + percent);
            }
        });*/
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
