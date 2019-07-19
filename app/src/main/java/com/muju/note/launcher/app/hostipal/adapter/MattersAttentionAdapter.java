package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.MattersAttentionBean;

import java.util.List;

public class MattersAttentionAdapter extends BaseQuickAdapter<MattersAttentionBean, BaseViewHolder> {

    public MattersAttentionAdapter(int layoutResId, @Nullable List<MattersAttentionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MattersAttentionBean masttersAttentionBean) {
        baseViewHolder.setText(R.id.tv_pos,masttersAttentionBean.getPos()+"");
        baseViewHolder.setText(R.id.tv_title,masttersAttentionBean.getTitle());
        baseViewHolder.setText(R.id.tv_content,masttersAttentionBean.getContent());
    }
}
