package com.muju.note.launcher.app.hostipal.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.ZkRecycleViewCostAdapter;
import com.muju.note.launcher.app.hostipal.bean.CostInfo;
import com.muju.note.launcher.app.hostipal.costquery.CustDatePick;
import com.muju.note.launcher.app.hostipal.costquery.RecycleViewDivider;
import com.muju.note.launcher.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CostFragment extends BaseFragment {
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_cost_money)
    TextView tvCostMoney;
    @BindView(R.id.tv_advance_gold)
    TextView tvAdvanceGold;
    @BindView(R.id.tv_advance_golds)
    TextView tvAdvanceGolds;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.bt_advance_record)
    Button btAdvanceRecord;
    @BindView(R.id.rl_advance_record)
    RelativeLayout rlAdvanceRecord;
    @BindView(R.id.tv_date_pick1)
    TextView tvDatePick1;
    @BindView(R.id.tv_date_pick2)
    TextView tvDatePick2;
    @BindView(R.id.tv_totle_money)
    TextView tvTotleMoney;
    @BindView(R.id.ll_advances)
    LinearLayout llAdvances;
    @BindView(R.id.ll_cost_title)
    LinearLayout llCostTitle;
    @BindView(R.id.zkrecycleview)
    RecyclerView mZkrecycleview;
    Unbinder unbinder;
    List<CostInfo> mList = new ArrayList<>();
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    private CustDatePick mCustDatePick;

    @Override
    public int getLayout() {
        return R.layout.fragment_cost;
    }

    @Override
    public void initData() {
        tvTitle.setText("费用详情");
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));

        initRecyclerView();
        mCustDatePick = new CustDatePick(getActivity());
        mList.clear();
        for (int i = 0; i < 10; i++) {
            mList.add(new CostInfo("注射XX", "2", "瓶", "20.00", "40.00元", "2018.01." + i));
        }
    }

    private void initRecyclerView() {
        mZkrecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //创建默认的线性LayoutManager
        mZkrecycleview.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mZkrecycleview.addItemDecoration(new DividerItemDecoration(mContext,
// DividerItemDecoration.VERTICAL, 1, getResources().getColor(R.color.default_bg));//设置分割线
        mZkrecycleview.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.table_line)));
        //设置分割线
        ZkRecycleViewCostAdapter adapter = new ZkRecycleViewCostAdapter(getActivity(), mList);
        mZkrecycleview.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.tv_date_pick1, R.id.tv_date_pick2, R.id.bt_advance_record, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date_pick1:
                mCustDatePick.show(tvDatePick1);
                break;
            case R.id.tv_date_pick2:
                mCustDatePick.show(tvDatePick2);
                break;
            case R.id.bt_advance_record:
                start(new AdvanceRecordFragment());
                break;
            case R.id.ll_back:
                pop();
                break;
        }
    }

}
