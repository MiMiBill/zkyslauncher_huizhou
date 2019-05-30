package com.muju.note.launcher.app.userinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.LuckDrawActivity;
import com.muju.note.launcher.app.userinfo.SigninActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.user.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserInfoFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.iv_signin)
    ImageView ivSignin;
    @BindView(R.id.iv_luckdraw)
    ImageView ivLuckdraw;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_userinfo;
    }


    public void initData() {
        try {
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getNickname())) {
                tvName.setText(UserUtil.getUserBean().getNickname());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getSex())) {
                tvSex.setText(UserUtil.getUserBean().getSex());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getMobile())) {
                tvPhone.setText(UserUtil.getUserBean().getMobile());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAddress())) {
                tvAddress.setText(UserUtil.getUserBean().getAddress());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAvater())) {
                Glide.with(getActivity()).load(UserUtil.getUserBean().getAvater()).apply
                        (RequestOptions.bitmapTransform(new CircleCrop())).into(ivHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initPresenter() {

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

    @OnClick({R.id.iv_signin, R.id.iv_luckdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_signin:
                startActivity(new Intent(getActivity(), SigninActivity.class));
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.iv_luckdraw:
                startActivity(new Intent(getActivity(), LuckDrawActivity.class));
                getActivity().overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void showError(String msg) {

    }
}
