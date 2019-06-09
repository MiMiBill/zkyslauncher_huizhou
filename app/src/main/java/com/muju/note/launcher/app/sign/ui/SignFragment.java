package com.muju.note.launcher.app.sign.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.sign.contract.SignContract;
import com.muju.note.launcher.app.sign.presenter.SignPresenter;
import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.user.UserUtil;

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
    private boolean isSign = false;
    private List<TaskListBean> adList=new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_signin;
    }

    @Override
    public void initData() {
        mPresenter.checkSignStatus(UserUtil.getUserBean().getId());
        tvIntegral.setText("您总共有" + UserUtil.getUserBean().getIntegral() + "积分");

        adList = SPUtil.getTaskList(Constants.AD_TASK_LIST);
    }

    @Override
    public void initPresenter() {
        mPresenter = new SignPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.tv_sign, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign:
                if (!isSign){
                    mPresenter.checkSign(UserUtil.getUserBean().getId());
                }
                break;
            case R.id.ll_back:
                pop();
                break;
        }
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
}
