package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CostQueryFragment extends BaseFragment {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_usersex)
    TextView tvUsersex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_userage)
    TextView tvUserage;
    @BindView(R.id.tv_sickbed_number)
    TextView tvSickbedNumber;
    @BindView(R.id.tv_user_sickbed_number)
    TextView tvUserSickbedNumber;
    @BindView(R.id.tv_inpatient_number)
    TextView tvInpatientNumber;
    @BindView(R.id.tv_user_inpatient_number)
    TextView tvUserInpatientNumber;
    @BindView(R.id.fl_cost_query)
    FrameLayout flCostQuery;
    Unbinder unbinder;

    @Override
    public int getLayout() {
        return R.layout.fragment_cost_query;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
    }
}
