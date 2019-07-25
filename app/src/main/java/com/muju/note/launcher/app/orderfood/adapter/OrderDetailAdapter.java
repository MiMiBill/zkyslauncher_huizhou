package com.muju.note.launcher.app.orderfood.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.util.gilde.GlideUtil;

import java.util.List;

public class OrderDetailAdapter extends BaseQuickAdapter<OrderListBean.ItemsBean, BaseViewHolder> {

    public OrderDetailAdapter(@Nullable List<OrderListBean.ItemsBean> data) {
        super(R.layout.item_food_order_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean.ItemsBean item) {

        helper.setText(R.id.tv_name, item.getCommodity().getName());
        helper.setText(R.id.tv_price, "￥"+item.getCommodity().getPrice());
        helper.setText(R.id.tv_count, "X"+item.getCount());

        GlideUtil.loadImg(item.getCommodity().getImages(),(ImageView)helper.getView(R.id.iv_img),R.mipmap.ic_video_load_default);
    }
}
