package com.muju.note.launcher.app.video.ui;

import com.muju.note.launcher.base.BaseFragment;

public class VideoContentFragment extends BaseFragment {

    private static final String TAG="VideoContentFragment";

    public static VideoContentFragment videoContentFragment;
    public static VideoContentFragment getIntance(){
        if(videoContentFragment==null){
            videoContentFragment=new VideoContentFragment();
        }
        return videoContentFragment;
    }

    @Override
    public int getLayout() {
        return 0;
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
