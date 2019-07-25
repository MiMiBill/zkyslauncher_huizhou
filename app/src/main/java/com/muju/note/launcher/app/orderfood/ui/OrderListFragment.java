package com.muju.note.launcher.app.orderfood.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.adapter.OrderHisAdapter;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.app.orderfood.contract.OrderListContract;
import com.muju.note.launcher.app.orderfood.presenter.OrderListPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DensityUtil;
import com.muju.note.launcher.util.VerticalLineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderListFragment extends BaseFragment<OrderListPresenter> implements OrderListContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    private OrderHisAdapter orderHisAdapter;
    private List<OrderListBean> dataBeanList=new ArrayList<>();
    @Override
    public int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void initData() {

        tvTitle.setText("订单历史");
        mPresenter.tabList(ActiveUtils.getPadActiveInfo().getBedId());
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.addItemDecoration(new VerticalLineDecoration(DensityUtil.dip2px(getActivity(), 20), true));
        orderHisAdapter=new OrderHisAdapter(dataBeanList);
        rvOrder.setAdapter(orderHisAdapter);
        orderHisAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListBean orderListBean = dataBeanList.get(position);
                start(OrderDetailFragment.newInstance(orderListBean));
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter=new OrderListPresenter();
    }

    @Override
    public void showError(String msg) {

    }



    @OnClick({R.id.ll_back, R.id.ll_null})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
            case R.id.ll_null:
                pop();
                break;
        }
    }

    @Override
    public void tabList(List<OrderListBean> list) {
        dataBeanList.clear();
        dataBeanList.addAll(list);
        orderHisAdapter.setNewData(dataBeanList);
    }
}
