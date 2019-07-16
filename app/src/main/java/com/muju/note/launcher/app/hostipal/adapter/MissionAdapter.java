package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.util.gilde.GlideUtil;

import java.util.List;

public class MissionAdapter extends BaseQuickAdapter<MissionInfoDao, BaseViewHolder> {

    public MissionAdapter(int layoutResId, @Nullable List<MissionInfoDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MissionInfoDao item) {
        GlideUtil.loadImg(item.getImg(), (ImageView) helper.getView(R.id.iv_img), R.mipmap.ic_video_load_default);
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_date, item.getUpdateTime());
    }
}
