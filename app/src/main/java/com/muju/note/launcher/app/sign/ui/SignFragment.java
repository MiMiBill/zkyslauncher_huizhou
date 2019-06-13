package com.muju.note.launcher.app.sign.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.app.sign.contract.SignContract;
import com.muju.note.launcher.app.sign.presenter.SignPresenter;
import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.entity.AdvertWebEntity;
import com.muju.note.launcher.util.user.UserUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//签到
public class SignFragment extends BaseFragment<SignPresenter> implements SignContract.View {
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_gift)
    TextView tvGift;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.iv_pub)
    ImageView ivPub;
    private boolean isSign = false;
    private List<TaskListBean> adList = new ArrayList<>();
    private TaskListBean videoBean;
    private TaskListBean pubBean;
    private TaskListBean signBean;
    private LoginDialog loginDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_signin;
    }

    @Override
    public void initData() {
        if (UserUtil.getUserBean() != null) {
            setTask();
        }

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void setTask() {
        mPresenter.checkSignStatus(UserUtil.getUserBean().getId());
//        tvIntegral.setText("您总共有" + UserUtil.getUserBean().getIntegral() + "积分");
       /* adList = SPUtil.getTaskList(Constants.AD_TASK_LIST);
        for (TaskListBean bean : adList) {
            if (bean.getTaskType() == 2) {
                videoBean=bean;
            } else if (bean.getTaskType() == 1) {
                pubBean=bean;
            }else if(bean.getTaskType()==3){
                signBean=bean;
            }
        }*/
    }

    @Override
    public void initPresenter() {
        mPresenter = new SignPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.tv_sign, R.id.iv_video, R.id.iv_pub})
    public void onViewClicked(View view) {
        if (UserUtil.getUserBean() == null) {
            showLoginDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.tv_sign:
                if (!isSign) {
                    mPresenter.checkSign(UserUtil.getUserBean().getId());
                }
                break;
            case R.id.iv_video:
                if (videoBean != null) {
                    EventBus.getDefault().post(new AdvertWebEntity(videoBean.getId(), videoBean
                            .getName(), videoBean.getResourceUrl(), 3));
                }
                break;
            case R.id.iv_pub:
                if (pubBean != null) {

                }
                break;
        }
    }


    private void showLoginDialog() {
        loginDialog = new LoginDialog(getActivity(), R.style.DialogFullscreen, new LoginDialog
                .OnLoginListener() {
            @Override
            public void onSuccess() {
                loginDialog.dismiss();
                setTask();
            }

            @Override
            public void onFail() {

            }
        });
        loginDialog.show();
    }

    @Override
    public void chesignStatus(SignStatusBean bean) {
        if (bean.getIsSign() == 1) {
            isSign = true;
            tvSign.setText("已签到");
        } else {
            tvSign.setText("签到");
        }
    }

    @Override
    public void checkSign(SignBean bean) {
        tvIntegral.setText("您总共有" + bean.getIntegral() + "积分");
        UserUtil.getUserBean().setIntegral(bean.getIntegral());
        tvSign.setText("已签到");
    }

    @Override
    public void doTask(TaskBean taskBean) {

    }
}
