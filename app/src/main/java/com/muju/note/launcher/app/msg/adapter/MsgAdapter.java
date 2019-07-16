package com.muju.note.launcher.app.msg.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.msg.db.CustomMessageDao;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<CustomMessageDao, BaseViewHolder> {
    public MsgAdapter(int layoutResId, @Nullable List<CustomMessageDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomMessageDao item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_date, item.getTime());
    }
}
