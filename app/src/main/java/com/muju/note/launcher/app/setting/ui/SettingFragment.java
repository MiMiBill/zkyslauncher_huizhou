package com.muju.note.launcher.app.setting.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.url.UrlUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.lly_user)
    LinearLayout llyUser;
    @BindView(R.id.lly_ma)
    LinearLayout llyMa;
    @BindView(R.id.lly_message)
    LinearLayout llyMessage;
    @BindView(R.id.lly_voice)
    LinearLayout llyVoice;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    Unbinder unbinder;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.iv_ma)
    ImageView ivMa;
    @BindView(R.id.tv_ma)
    TextView tvMa;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initData() {
        replaceFragment(0);
        tvVersion.setText(String.format("宝屏V%s", TextUtils.equals(UrlUtil.getHost(), "http://test.pad.zgzkys.com") ? BuildConfig.VERSION_NAME + "beta" : BuildConfig.VERSION_NAME));
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.lly_user, R.id.lly_ma, R.id.lly_message, R.id.lly_voice,R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_user:
                replaceFragment(0);
                break;
            case R.id.lly_ma:
                replaceFragment(1);
                break;
            case R.id.lly_message:
                replaceFragment(3);
                break;
            case R.id.lly_voice:
                replaceFragment(4);
                break;
            case R.id.ll_back:
                pop();
                break;
        }
    }

    private void replaceFragment(int type) {
        Fragment fragment = null;
        switch (type) {
            case 0:
                fragment = new UserFragment();
                setBackGround(0);
                break;
            case 1:
                fragment = new ToolFragment();
                setBackGround(1);
                break;
            case 3:
                fragment = new FeedBackFragment();
                setBackGround(3);
                break;
            case 4:
                fragment = new VoiceFragment();
                setBackGround(4);
                break;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setBackGround(int type) {
        switch (type) {
            case 0:
                llyUser.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMa.setBackgroundColor(getResources().getColor(R.color.white));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.white));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.white));

                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvMa.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMessage.setTextColor(getResources().getColor(R.color.blue_gray));
                tvVoice.setTextColor(getResources().getColor(R.color.blue_gray));

                ivUser.setImageResource(R.mipmap.setting_user);
                ivMa.setImageResource(R.mipmap.setting_ma_select);
                ivMessage.setImageResource(R.mipmap.setting_message_select);
                ivVoice.setImageResource(R.mipmap.setting_voice_select);
                break;
            case 1:
                llyUser.setBackgroundColor(getResources().getColor(R.color.white));
                llyMa.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.white));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.white));

                tvUser.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMa.setTextColor(getResources().getColor(R.color.white));
                tvMessage.setTextColor(getResources().getColor(R.color.blue_gray));
                tvVoice.setTextColor(getResources().getColor(R.color.blue_gray));

                ivUser.setImageResource(R.mipmap.setting_user_select);
                ivMa.setImageResource(R.mipmap.setting_qu);
                ivMessage.setImageResource(R.mipmap.setting_message_select);
                ivVoice.setImageResource(R.mipmap.setting_voice_select);
                break;
            case 3:
                llyUser.setBackgroundColor(getResources().getColor(R.color.white));
                llyMa.setBackgroundColor(getResources().getColor(R.color.white));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.blue_gray));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.white));

                tvUser.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMa.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMessage.setTextColor(getResources().getColor(R.color.white));
                tvVoice.setTextColor(getResources().getColor(R.color.blue_gray));

                ivUser.setImageResource(R.mipmap.setting_user_select);
                ivMa.setImageResource(R.mipmap.setting_ma_select);
                ivMessage.setImageResource(R.mipmap.setting_message);
                ivVoice.setImageResource(R.mipmap.setting_voice_select);
                break;
            case 4:
                llyUser.setBackgroundColor(getResources().getColor(R.color.white));
                llyMa.setBackgroundColor(getResources().getColor(R.color.white));
                llyMessage.setBackgroundColor(getResources().getColor(R.color.white));
                llyVoice.setBackgroundColor(getResources().getColor(R.color.blue_gray));

                tvUser.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMa.setTextColor(getResources().getColor(R.color.blue_gray));
                tvMessage.setTextColor(getResources().getColor(R.color.blue_gray));
                tvVoice.setTextColor(getResources().getColor(R.color.white));

                ivUser.setImageResource(R.mipmap.setting_user_select);
                ivMa.setImageResource(R.mipmap.setting_ma_select);
                ivMessage.setImageResource(R.mipmap.setting_message_select);
                ivVoice.setImageResource(R.mipmap.setting_voice);
                break;
        }
    }

}
