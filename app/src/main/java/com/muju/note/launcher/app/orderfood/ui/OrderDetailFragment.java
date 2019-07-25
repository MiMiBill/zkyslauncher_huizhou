package com.muju.note.launcher.app.orderfood.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.adapter.OrderDetailAdapter;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.app.orderfood.presenter.OrderListPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.DensityUtil;
import com.muju.note.launcher.util.VerticalLineDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailFragment extends BaseFragment {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    private static final String ORDERLIST_BEAN="orderListBean";
    private OrderDetailAdapter orderDetailAdapter;
    private OrderListBean orderListBean;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_detail;
    }

    public static OrderDetailFragment newInstance(OrderListBean orderListBean) {
        Bundle args = new Bundle();
        args.putSerializable(ORDERLIST_BEAN, orderListBean);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData() {
        tvTitle.setText("订单详情");
        Bundle args = getArguments();
        if (args != null) {
            orderListBean = (OrderListBean) args.getSerializable(ORDERLIST_BEAN);
        }
        List<OrderListBean.ItemsBean> items = orderListBean.getItems();
        orderDetailAdapter=new OrderDetailAdapter(items);
        rvOrder.setAdapter(orderDetailAdapter);
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
        rvOrder.addItemDecoration(new VerticalLineDecoration(DensityUtil.dip2px(getActivity(), 2), true));
    }

    @Override
    public void initPresenter() {
        mPresenter=new OrderListPresenter();
    }

    @Override
    public void showError(String msg) {

    }



    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                break;
        }
    }


}
