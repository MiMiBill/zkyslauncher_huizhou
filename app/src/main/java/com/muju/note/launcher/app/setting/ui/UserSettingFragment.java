package com.muju.note.launcher.app.setting.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.muju.note.launcher.service.updateversion.UpdateVersionService;
import com.muju.note.launcher.url.UrlUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

//个人中心
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
    private int selectCheckedId = -1;
    private UserFragment userFragment;
    private FeedBackFragment feedBackFragment;
    private ToolFragment toolFragment;
    private GuideFragment guideFragment;
    private VoiceFragment voiceFragment;
    private RadioButton firstRadio;

    @Override
    public int getLayout() {
        return R.layout.fragment__user_setting;
    }

    @Override
    public void initData() {
        tvTitle.setText("个人中心");
        tvVersion.setText(String.format("安屏V%s", TextUtils.equals(UrlUtil.getHost(), "http://pad.test.zgzkys.com") ? BuildConfig.VERSION_NAME + "beta" : BuildConfig.VERSION_NAME));

        tvVersion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                HideActivity.launch(LauncherApplication.getContext());
                toHide();
                return true;
            }
        });

        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        firstRadio = (RadioButton) radioGroup.getChildAt(0);
        firstRadio.setChecked(true);

    }

    private void toHide() {
        final EditText et = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(et)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et.getText().toString().equals("147258369")) {
                            HideActivity.launch(LauncherApplication.getContext());
                        } else {
                            showToast("验证码错误");
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                et.setText("");
            }
        });
        dialog.show();
    }


    RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (selectCheckedId == checkedId) {
                return;
            }
            Fragment showFg = null;
            Fragment hideFg = null;
            switch (selectCheckedId) {
                case R.id.rbt_user:
                    hideFg = userFragment;
                    break;
                case R.id.rbt_tool:
                    hideFg = toolFragment;
                    break;
                case R.id.rbt_feedback:
                    hideFg = feedBackFragment;
                    break;
                case R.id.rbt_guide:
                    hideFg = guideFragment;
                    break;
                case R.id.rbt_setting:
                    hideFg = voiceFragment;
                    break;
            }
            switch (checkedId) {
                case R.id.rbt_user:
                    showFg = getUserFragment();
                    break;
                case R.id.rbt_tool:
                    showFg = getToolFragment();
                    break;
                case R.id.rbt_feedback:
                    showFg = getFeedBackFragment();
                    break;
                case R.id.rbt_guide:
                    showFg = getGuideFragment();
                    break;
                case R.id.rbt_setting:
                    showFg = getVoiceFragment();
                    break;
            }
            selectCheckedId = checkedId;
            addShowOrHideFragment(showFg, hideFg);
        }
    };


    private UserFragment getUserFragment() {
        if (userFragment == null) {
            userFragment = UserFragment.getInstance();
        }
        return userFragment;
    }

    private ToolFragment getToolFragment() {
        if (toolFragment == null) {
            toolFragment = ToolFragment.getInstance();
        }
        return toolFragment;
    }

    private FeedBackFragment getFeedBackFragment() {
        if (feedBackFragment == null) {
            feedBackFragment = FeedBackFragment.getInstance();
        }
        return feedBackFragment;
    }

    private VoiceFragment getVoiceFragment() {
        if (voiceFragment == null) {
            voiceFragment = VoiceFragment.newInstance(2);
        }
        return voiceFragment;
    }

    private GuideFragment getGuideFragment() {
        if (guideFragment == null) {
            guideFragment = GuideFragment.newInstance(2);
        }
        return guideFragment;
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    private void addShowOrHideFragment(Fragment showFg, Fragment hideFg) {
        FragmentTransaction tran = getChildFragmentManager().beginTransaction();
        if (showFg.isAdded()) {
            tran.show(showFg);
        } else {
            tran.add(R.id.fl_container, showFg);
        }
        if (hideFg != null) {
            tran.hide(hideFg);
        }
        tran.commitAllowingStateLoss();
    }


    @OnClick({R.id.ll_back,R.id.btn_update_version})
    public void onViewClicked(View view) {

        switch (view.getId())
        {
            case R.id.ll_back:
            {
                pop();
                break;
            }
            case R.id.btn_update_version:
            {
                // 查询更新
                UpdateVersionService.getInstance().start();

            }
        }

    }

}
