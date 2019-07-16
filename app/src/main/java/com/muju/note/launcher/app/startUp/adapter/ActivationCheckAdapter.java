package com.muju.note.launcher.app.startUp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;

import java.util.List;

public class ActivationCheckAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ActivationCheckAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_msg, item);
    }
}
