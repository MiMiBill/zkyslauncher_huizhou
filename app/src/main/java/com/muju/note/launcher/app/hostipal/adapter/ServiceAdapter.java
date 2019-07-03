package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.Entity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class ServiceAdapter extends BaseQuickAdapter<Entity, BaseViewHolder> {
    public ServiceAdapter(@Nullable List<Entity> data) {
        super(R.layout.item_medical_service, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Entity item) {

        helper.setImageResource(R.id.img, item.getIcon());
    }
}
