package com.muju.note.launcher.app.sign.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.sign.contract.SignContract;
import com.muju.note.launcher.app.sign.presenter.SignPresenter;
import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.user.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignFragment extends BaseFragment<SignPresenter> implements SignContract.View {
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_gift)
    TextView tvGift;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    Unbinder unbinder;
    private boolean isSign = false;

    @Override
    public int getLayout() {
        return R.layout.activity_signin;
    }

    @Override
    public void initData() {
        mPresenter.checkSignStatus(UserUtil.getUserBean().getId());
        tvIntegral.setText("您总共有" + UserUtil.getUserBean().getIntegral() + "积分");
    }

    @Override
    public void initPresenter() {
        mPresenter = new SignPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    //检查签到状态
    private void checkSignStatus() {
        OkGo.<BaseBean<SignStatusBean>>get(String.format(UrlUtil.checkSignStatus(), UserUtil
                .getUserBean().getId()))
                .tag(this)
                .execute(new JsonCallback<BaseBean<SignStatusBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<SignStatusBean>> response) {
                        if (response.body().getData() instanceof SignStatusBean) {
                            SignStatusBean bean = response.body().getData();
                            if (bean.getIsSign() == 1) {
                                isSign = true;
                                tvSign.setText("已签到");
                            } else {
                                tvSign.setText("签到");
                            }
                        } else {
                            tvSign.setText("签到");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_sign, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign:
                if (!isSign)
                    mPresenter.checkSign(UserUtil.getUserBean().getId());
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
    }
}
