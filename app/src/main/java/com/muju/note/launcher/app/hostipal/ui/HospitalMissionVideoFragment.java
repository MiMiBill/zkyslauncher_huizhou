package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.VideoPlayControlView;

import java.io.File;

import butterknife.BindView;

/**
 * 宣教视频播放页面
 */
public class HospitalMissionVideoFragment extends BaseFragment implements VideoPlayControlView.BrightnessChangeListener, VideoPlayControlView.BackListener {
    @BindView(R.id.video_view)
    VideoPlayControlView videoView;

    public static final String MISSION_VIDEO_PATH = "mission_video_path";

    public static HospitalMissionVideoFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(MISSION_VIDEO_PATH, path);
        HospitalMissionVideoFragment fragment = new HospitalMissionVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    @Override
    public int getLayout() {
        return R.layout.fragment_his_mission_video;
    }

    @Override
    public void initData() {

        mWindow = getActivity().getWindow();
        mLayoutParams = mWindow.getAttributes();
        videoView.setBrightnessChangeListener(this);
        videoView.setBackListener(this);

        File file = new File(SdcardConfig.RESOURCE_FOLDER, getArguments().get(MISSION_VIDEO_PATH).hashCode() + ".mp4");
        if (!file.exists()) {
            showToast("文件路径不存在，请稍后重试");
            return;
        }
        videoView.getVideoView().setVideoPath(file.getPath());
        videoView.getVideoView().start();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void back() {
        pop();
    }

    @Override
    public void changeBrightness(float brightness) {
        mLayoutParams.screenBrightness = brightness;
        mWindow.setAttributes(mLayoutParams);
    }

    @Override
    public float getBrightness() {
        if (mLayoutParams.screenBrightness == -1) {
            return SystemUtils.getScreenBrightness();
        }
        return mLayoutParams.screenBrightness;
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.getVideoView().suspend();
            videoView.onDestroy();
        }
    }
}
