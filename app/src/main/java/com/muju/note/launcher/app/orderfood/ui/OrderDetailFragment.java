package com.muju.note.launcher.app.orderfood.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.orderfood.adapter.OrderDetailAdapter;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.app.orderfood.presenter.OrderListPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ActiveUtils;
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
    private static final String ORDERLIST_BEAN = "orderListBean";
    @BindView(R.id.tv_hos)
    TextView tvHos;
    @BindView(R.id.tv_totle)
    TextView tvTotle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
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
        ActivePadInfo.DataBean activeInfo = ActiveUtils.getPadActiveInfo();
        tvHos.setText(activeInfo.getHospitalName());
        List<OrderListBean.ItemsBean> items = orderListBean.getItems();
        orderDetailAdapter = new OrderDetailAdapter(items);
        rvOrder.setAdapter(orderDetailAdapter);
        tvAddress.setText(activeInfo.getHospitalName() + activeInfo.getDeptName() + activeInfo
                .getBedNumber() + "床");
        tvName.setText(orderListBean.getName() + " " + orderListBean.getMobile());
        double totalAmount = orderListBean.getTotalAmount();

        setTotal(totalAmount);
    }

    private void setTotal(double totalAmount) {
        String total="合计: ￥"+totalAmount;
        SpannableStringBuilder sb = new SpannableStringBuilder(total);
        sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_585858)), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_FF3000)), total.length()-5,
                total.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(1.5f), total.length()-5, total.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTotle .setText(sb);
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
        rvOrder.addItemDecoration(new VerticalLineDecoration(DensityUtil.dip2px(getActivity(), 2)
                , true));
    }

    @Override
    public void initPresenter() {
        mPresenter = new OrderListPresenter();
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
