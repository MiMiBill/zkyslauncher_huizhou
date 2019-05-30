package com.muju.note.launcher.app.setting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.muju.note.launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends FragmentActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.lly_user)
    LinearLayout llyUser;
    @BindView(R.id.lly_ma)
    LinearLayout llyMa;
    @BindView(R.id.lly_guide)
    LinearLayout llyGuide;
    @BindView(R.id.lly_message)
    LinearLayout llyMessage;
    @BindView(R.id.lly_voice)
    LinearLayout llyVoice;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.fragment_setting);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_back, R.id.lly_user, R.id.lly_ma, R.id.lly_guide, R.id.lly_message, R.id
            .lly_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_user:
                replaceFragment(0);
                break;
            case R.id.lly_ma:
                replaceFragment(1);
                break;
            case R.id.lly_guide:
                replaceFragment(2);
                break;
            case R.id.lly_message:
                replaceFragment(3);
                break;
            case R.id.lly_voice:
                replaceFragment(4);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }


    private void replaceFragment(int type) {
        Fragment fragment=null;
        switch (type){
            case 0:
                fragment=new UserFragment();
                setBackGround(0);
                break;
            case 1:
                fragment=new ToolFragment();
                setBackGround(1);
                break;
            case 2:
                fragment=new GuideFragment();
                setBackGround(2);
                break;
            case 3:
                fragment=new FeedBackFragment();
                setBackGround(3);
                break;
            case 4:
                fragment=new VoiceFragment();
                setBackGround(4);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setBackGround(int type) {
        switch (type){
            case 0:
                llyUser.setBackgroundColor(getResources().getColor(R.color.white));
                llyMa.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyGuide.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                break;
            case 1:
                llyUser.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMa.setBackgroundColor(getResources().getColor(R.color.white));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyGuide.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                break;
            case 2:
                llyUser.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMa.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyGuide.setBackgroundColor(getResources().getColor(R.color.white));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                break;
            case 3:
                llyUser.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMa.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.white));
                llyGuide.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                break;
            case 4:
                llyUser.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMa.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyGuide.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }
}
