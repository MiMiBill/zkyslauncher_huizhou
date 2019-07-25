package com.muju.note.launcher.app.orderfood.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;

import java.util.List;

public class OrderHisAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {

    public OrderHisAdapter( @Nullable List<OrderListBean> data) {
        super(R.layout.item_food_order_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean item) {

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_phone, item.getMobile());
        helper.setText(R.id.tv_hos, item.getHospitalName()+item.getDeptName()+item.getBedNumber()+"床");
        helper.setText(R.id.tv_price, "￥"+item.getTotalAmount());

        helper.addOnClickListener(R.id.lly_order);
    }
}
