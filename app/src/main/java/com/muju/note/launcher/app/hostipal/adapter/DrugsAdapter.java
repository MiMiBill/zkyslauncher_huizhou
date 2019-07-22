package com.muju.note.launcher.app.hostipal.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.DrugsBean;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.List;

public class DrugsAdapter extends BaseQuickAdapter<DrugsBean, BaseViewHolder> {

    public DrugsAdapter(int layoutResId, @Nullable List<DrugsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DrugsBean drugsBean) {
        TextView tvName=baseViewHolder.getView(R.id.tv_name);
        ImageView ivIcon=baseViewHolder.getView(R.id.iv_icon);
        if(baseViewHolder.getAdapterPosition()==0){
            baseViewHolder.getView(R.id.view_round).setSelected(true);
            tvName.setTextColor(Color.parseColor("#41BAEF"));
            ivIcon.setImageResource(R.mipmap.ic_his_service_drugs);
        }else {
            baseViewHolder.getView(R.id.view_round).setSelected(false);
            tvName.setTextColor(Color.parseColor("#585858"));
            ivIcon.setImageResource(R.mipmap.ic_his_service_drugs_black);
        }
        baseViewHolder.setText(R.id.tv_date,drugsBean.getDate());
        tvName.setText(drugsBean.getName());

        RecyclerView rvDrugsSub=baseViewHolder.getView(R.id.rv_drugs_sub);
        DrugsSubAdapter subAdapter=new DrugsSubAdapter(R.layout.item_rv_drugs_sub,drugsBean.getData());
        subAdapter.setPos(baseViewHolder.getAdapterPosition());
        rvDrugsSub.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),LinearLayoutManager.VERTICAL,false));
        rvDrugsSub.setAdapter(subAdapter);
    }
}
