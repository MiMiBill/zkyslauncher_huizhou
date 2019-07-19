package com.muju.note.launcher.app.hostipal.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 医疗服务
 */
public class HospitalelServiceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "HospitalelEganceFragment";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_cost)
    LinearLayout llCost;
    @BindView(R.id.btn_cost1)
    Button btnCost1;
    @BindView(R.id.btn_cost2)
    Button btnCost2;
    @BindView(R.id.btn_cost3)
    Button btnCost3;
    @BindView(R.id.btn_cost4)
    Button btnCost4;
    @BindView(R.id.btn_cost5)
    Button btnCost5;
    @BindView(R.id.ll_chemotherapay)
    LinearLayout llChemotherapay;
    @BindView(R.id.ll_matters_attention)
    LinearLayout llMattersAttention;


    @Override
    public int getLayout() {
        return R.layout.fragment_new_his_service;
    }

    @Override
    public void initData() {
        llCost.setOnClickListener(this);
        btnCost1.setOnClickListener(this);
        btnCost2.setOnClickListener(this);
        btnCost3.setOnClickListener(this);
        btnCost4.setOnClickListener(this);
        btnCost5.setOnClickListener(this);
        llChemotherapay.setOnClickListener(this);
        llMattersAttention.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cost:
                start(new CostFragment());
                break;
            case R.id.btn_cost1:
            case R.id.btn_cost2:
            case R.id.btn_cost3:
            case R.id.btn_cost4:
            case R.id.btn_cost5:
                start(new CostFragment());
                break;

            case R.id.ll_chemotherapay:
                start(new ChemotherapyFragment());
                break;

            case R.id.ll_matters_attention:
                start(new MattersAttentionFragment());
                break;
        }
    }

}
