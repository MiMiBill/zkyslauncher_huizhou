package com.muju.note.launcher.app.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.contract.HomeContract;
import com.muju.note.launcher.app.home.presenter.HomePresenter;
import com.muju.note.launcher.app.hostipal.ui.EncyclopediasFragment;
import com.muju.note.launcher.app.hostipal.ui.HospitalMienFragment;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View
        .OnClickListener {

    private static final String TAG = "HomeFragment";

    public static HomeFragment homeFragment = null;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.ll_hostipal)
    LinearLayout llHostipal;
    @BindView(R.id.ll_hostipal_ency)
    LinearLayout llHostipalEncy;
    Unbinder unbinder;

    public static HomeFragment newInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {
        llHostipal.setOnClickListener(this);
        llHostipalEncy.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {
        mPresenter = new HomePresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.updateDate();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mPresenter.onDestroy();
    }

    /**
     * 设置状态栏时间
     *
     * @param date
     * @param time
     * @param week
     */
    @Override
    public void getDate(String date, String time, String week) {
        tvDate.setText(date);
        tvTime.setText(time);
        tvWeek.setText(week);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_hostipal:  // 医院风采
                start(HospitalMienFragment.getInstance());
                break;
            case R.id.ll_hostipal_ency:  // 医疗百科
                start(EncyclopediasFragment.getInstance());
                break;
        }
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
}
