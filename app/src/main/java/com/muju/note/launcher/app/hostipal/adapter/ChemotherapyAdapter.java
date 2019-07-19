package com.muju.note.launcher.app.hostipal.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.ChemotherapyBean;

import java.util.List;

public class ChemotherapyAdapter extends BaseQuickAdapter<ChemotherapyBean, BaseViewHolder> {
    public ChemotherapyAdapter(int layoutResId, @Nullable List<ChemotherapyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChemotherapyBean chemotherapyBean) {

        baseViewHolder.setText(R.id.tv_name,chemotherapyBean.getName());
        baseViewHolder.setText(R.id.tv_name_sub,chemotherapyBean.getNameSub());
        baseViewHolder.setText(R.id.tv_result,chemotherapyBean.getResult());
        baseViewHolder.setText(R.id.tv_result_sub,chemotherapyBean.getResultSub());
        baseViewHolder.setText(R.id.tv_example,chemotherapyBean.getExample());
        baseViewHolder.setText(R.id.tv_example_sub,chemotherapyBean.getExampleSub());
        baseViewHolder.setText(R.id.tv_company,chemotherapyBean.getCompany());
        baseViewHolder.setText(R.id.tv_company_sub,chemotherapyBean.getCompanySub());

        if(baseViewHolder.getAdapterPosition()%2==0){
            LinearLayout llItem=baseViewHolder.getView(R.id.ll_item);
            llItem.setBackgroundColor(Color.WHITE);
        }else {
            LinearLayout llItem=baseViewHolder.getView(R.id.ll_item);
            llItem.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        if(baseViewHolder.getAdapterPosition()==getData().size()-1){
            baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
        }else {
            baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
        }

    }
}
