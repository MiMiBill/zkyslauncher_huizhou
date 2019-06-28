package com.muju.note.launcher.app.setting.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.startUp.ui.HideActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserSettingFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rbt_user)
    RadioButton rbtUser;
    @BindView(R.id.rbt_order)
    RadioButton rbtOrder;
    @BindView(R.id.rbt_tool)
    RadioButton rbtTool;
    @BindView(R.id.rbt_feedback)
    RadioButton rbtFeedback;
    @BindView(R.id.rbt_guide)
    RadioButton rbtGuide;
    @BindView(R.id.rbt_setting)
    RadioButton rbtSetting;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    Unbinder unbinder;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @Override
    public int getLayout() {
        return R.layout.fragment__user_setting;
    }

    @Override
    public void initData() {
        rbtUser.setChecked(true);
        replaceFragment(0);
        tvTitle.setText("个人中心");
        tvVersion.setText(String.format("宝屏V%s", TextUtils.equals(UrlUtil.getHost(), "http://test" +
                ".pad.zgzkys.com") ? BuildConfig.VERSION_NAME + "beta" : BuildConfig.VERSION_NAME));

        tvVersion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HideActivity.launch(LauncherApplication.getContext());
                return true;
            }
        });
        radioGroup.setOnCheckedChangeListener(new MyCheckChangeListener());
    }


    private class MyCheckChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rbt_user:
                    replaceFragment(0);
                    break;
                case R.id.rbt_order:
                    replaceFragment(1);
                    break;
                case R.id.rbt_tool:
                    replaceFragment(2);
                    break;
                case R.id.rbt_feedback:
                    replaceFragment(3);
                    break;
                case R.id.rbt_guide:
                    replaceFragment(4);
                    break;
                case R.id.rbt_setting:
                    replaceFragment(5);
                    break;


            }
        }
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }




    private void replaceFragment(int type) {
        Fragment fragment = null;
        switch (type) {
            case 0:
                fragment = new UserFragment();
                break;
            case 1:
                fragment = new UserOrderFragment();
                break;
            case 2:
                fragment = new ToolFragment();
                break;
            case 3:
                fragment = new FeedBackFragment();
                break;
            case 4:
                fragment = GuideFragment.newInstance(2);
                break;
            case 5:
                fragment = new VoiceFragment();
                break;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }

}
