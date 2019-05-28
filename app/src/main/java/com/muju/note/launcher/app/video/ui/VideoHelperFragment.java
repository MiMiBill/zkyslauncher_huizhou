package com.muju.note.launcher.app.video.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoHelperFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    public int getLayout() {
        return R.layout.fragment_video_helper;
    }

    @Override
    public void initData() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }
}
