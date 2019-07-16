package com.muju.note.launcher.app.hostipal.ui;

import android.widget.LinearLayout;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.presenter.HospitalMienPresenter;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 医疗服务
 */
public class MedicalServiceFragment extends BaseFragment {
    private static final String TAG = "CabinetFragment";
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    @Override
    public int getLayout() {
        return R.layout.fragment_medical_service;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {
        mPresenter = new HospitalMienPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }
}
