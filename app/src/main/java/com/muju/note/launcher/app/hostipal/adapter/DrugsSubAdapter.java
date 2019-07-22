package com.muju.note.launcher.app.hostipal.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.DrugsSubBean;

import java.util.List;

public class DrugsSubAdapter extends BaseQuickAdapter<DrugsSubBean, BaseViewHolder> {

    private int pos=0;

    public DrugsSubAdapter(int layoutResId, @Nullable List<DrugsSubBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DrugsSubBean drugsSubBean) {
        baseViewHolder.setText(R.id.tv_pos,drugsSubBean.getPos()+"");
        baseViewHolder.setText(R.id.tv_name,drugsSubBean.getName());
        TextView tvContent=baseViewHolder.getView(R.id.tv_content);
        tvContent.setText(drugsSubBean.getContent());
        if(pos==0){
            tvContent.setTextColor(Color.parseColor("#FF633A"));
        }else {
            tvContent.setTextColor(Color.parseColor("#585858"));
        }
        TextView tvTag=baseViewHolder.getView(R.id.tv_tag);
        if(drugsSubBean.getTag()==1){
            tvTag.setSelected(true);
            tvTag.setText("内服");
        }else {
            tvTag.setText("外用");
            tvTag.setSelected(false);
        }

        if(pos!=0){
            tvTag.setBackgroundResource(R.drawable.shape_button_bg_white3);
        }
    }

    public void setPos(int pos){
        this.pos=pos;
    }
}
