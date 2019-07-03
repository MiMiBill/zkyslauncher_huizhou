package com.muju.note.launcher.app.hostipal.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.ZkRecycleViewAdvanceAdapter;
import com.muju.note.launcher.app.hostipal.bean.AdvanceRecordInfo;
import com.muju.note.launcher.app.hostipal.costquery.RecycleViewDivider;
import com.muju.note.launcher.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/6/14.
 */

public class AdvanceRecordFragment extends BaseFragment {
    @BindView(R.id.rcl_advance_record)
    RecyclerView mRclAdvanceRecord;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    Unbinder unbinder;
    private List<AdvanceRecordInfo> mList = new ArrayList<>();
    private ZkRecycleViewAdvanceAdapter advanceAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_advance_record_layout;
    }

    @Override
    public void initData() {
        tvTitle.setText("预交金记录");
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        initRecyclerView();
        for (int i = 0; i < 50; i++) {
            mList.add(new AdvanceRecordInfo("2018.6." + i, "1000.00元", "壹仟元", "支付宝", "老王"));
        }
        advanceAdapter.notifyDataSetChanged();
    }


    @Override
    public void initPresenter() {

    }

    private void initRecyclerView() {
        mRclAdvanceRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
        //创建默认的线性LayoutManager
        mRclAdvanceRecord.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mRclAdvanceRecord.addItemDecoration(new DividerItemDecoration(mContext,
// DividerItemDecoration.VERTICAL));//设置分割线
        mRclAdvanceRecord.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager
                .VERTICAL, 1, getResources().getColor(R.color.table_line)));//设置分割线
        advanceAdapter = new ZkRecycleViewAdvanceAdapter(getActivity(), mList);
        mRclAdvanceRecord.setAdapter(advanceAdapter);
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }
}
