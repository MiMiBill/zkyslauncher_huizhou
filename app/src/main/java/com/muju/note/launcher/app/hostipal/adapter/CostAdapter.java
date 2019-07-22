package com.muju.note.launcher.app.hostipal.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.CostBean;

import java.util.List;

public class CostAdapter extends BaseQuickAdapter<CostBean, BaseViewHolder> {

    public CostAdapter(int layoutResId, @Nullable List<CostBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CostBean costBean) {
        baseViewHolder.setText(R.id.tv_pos,costBean.getPos()+"");
        baseViewHolder.setText(R.id.tv_name,costBean.getName());
        baseViewHolder.setText(R.id.tv_size,costBean.getSize());
        baseViewHolder.setText(R.id.tv_num,costBean.getNum()+"");
        baseViewHolder.setText(R.id.tv_company,costBean.getCompany());
        baseViewHolder.setText(R.id.tv_price,costBean.getPrice());
        baseViewHolder.setText(R.id.tv_type,costBean.getType());
        baseViewHolder.setText(R.id.tv_level,costBean.getLevel());
        baseViewHolder.setText(R.id.tv_date,costBean.getDate());

        if(costBean.getPos()%2==0){
            LinearLayout llItem=baseViewHolder.getView(R.id.ll_item);
            llItem.setBackgroundColor(Color.WHITE);
        }else {
            LinearLayout llItem=baseViewHolder.getView(R.id.ll_item);
            llItem.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

    }
}
