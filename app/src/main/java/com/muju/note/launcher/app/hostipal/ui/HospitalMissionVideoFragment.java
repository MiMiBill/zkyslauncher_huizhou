package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.util.MissionDbUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
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
    public static final String MISSION_VIDEO_ID = "mission_video_id";
    public static final String MISSION_VIDEO_NAME = "mission_video_name";
    public static final String MISSION_VIDEO_PUSHID = "mission_video_push_id";

    private int missionId;
    private String missionName;
    private long startTime;
    private String pushId;

    public static HospitalMissionVideoFragment newInstance(String path,int missionId,String missionName,String pushId) {
        Bundle args = new Bundle();
        args.putString(MISSION_VIDEO_PATH, path);
        args.putInt(MISSION_VIDEO_ID, missionId);
        args.putString(MISSION_VIDEO_NAME, missionName);
        args.putString(MISSION_VIDEO_PUSHID, pushId);
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

        missionId=getArguments().getInt(MISSION_VIDEO_ID);
        missionName=getArguments().getString(MISSION_VIDEO_NAME);
        pushId=getArguments().getString(MISSION_VIDEO_PUSHID);
        startTime=System.currentTimeMillis();

        if(!TextUtils.isEmpty(pushId)){
            getUpdateReadFlag(pushId);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MissionDbUtil.getInstance().addData(missionId,missionName,startTime,System.currentTimeMillis());
    }

    /**
     *  修改为已读状态
     * @param pushId
     */
    public void getUpdateReadFlag(String pushId) {
        OkGo.<BaseBean<Void>>get(UrlUtil.getUpdateReadFlag()+pushId)
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {

                    }
                });
    }
}
