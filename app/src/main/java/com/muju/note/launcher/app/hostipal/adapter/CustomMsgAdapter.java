package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.CustomMsgBean;
import com.muju.note.launcher.app.hostipal.bean.MattersAttentionBean;

import java.util.List;

public class CustomMsgAdapter extends BaseQuickAdapter<CustomMsgBean, BaseViewHolder> {

    public CustomMsgAdapter(int layoutResId, @Nullable List<CustomMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomMsgBean bean) {
        baseViewHolder.setText(R.id.tv_pos,bean.getPos()+"");
        baseViewHolder.setText(R.id.tv_title,bean.getTitle());
        baseViewHolder.setText(R.id.tv_content,bean.getContent());
        baseViewHolder.setText(R.id.tv_date,bean.getDate());
    }
}
