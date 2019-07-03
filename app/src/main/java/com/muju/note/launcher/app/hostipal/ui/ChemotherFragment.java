package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.ChemotherapayFragmentAdapter;
import com.muju.note.launcher.app.hostipal.bean.ChemotherapayItemInfo;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.log.LogFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/19.
 */

public class ChemotherFragment extends BaseFragment {
    @BindView(R.id.rcl_chemotherapy)
    RecyclerView mRclChemotherapy;
    private List<ChemotherapayItemInfo> mList = new ArrayList<>();
    private ChemotherapayFragmentAdapter chemotherapayFragmentAdapter;

    @Override
    public int getLayout() {
        return R.layout.chemotherapay_fragment_layout;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogFactory.l().i("initData");
        initRecyclerView();
        mList.clear();
        for (int i = 0; i < 10; i++) {
            mList.add(new ChemotherapayItemInfo("维生素C", "0.5", "mmol/L", "正常", "0-1.0"));
        }
        chemotherapayFragmentAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {

        mRclChemotherapy.setLayoutManager(new LinearLayoutManager(getActivity()));//创建默认的线性LayoutManager
        mRclChemotherapy.setHasFixedSize(true);//如果可以确定每个ite的高度是固定的，设置这个选项可以提高性能
        //mRclChemotherapy.addItemDecoration(new DividerItemDecoration(mContext,
        // DividerItemDecoration.VERTICAL));//设置分割线
        chemotherapayFragmentAdapter = new ChemotherapayFragmentAdapter(getActivity(), mList);
        mRclChemotherapy.setAdapter(chemotherapayFragmentAdapter);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

}






















