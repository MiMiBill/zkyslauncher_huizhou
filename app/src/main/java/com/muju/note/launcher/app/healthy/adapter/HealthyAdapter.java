package com.muju.note.launcher.app.healthy.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.util.gilde.GlideUtil;

import java.util.List;

public class HealthyAdapter extends BaseQuickAdapter<VideoInfoDao,BaseViewHolder> {

    public HealthyAdapter(int layoutResId, @Nullable List<VideoInfoDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoInfoDao item) {
        GlideUtil.loadImg(item.getScreenUrl(), (ImageView) helper.getView(R.id.iv_img),R.mipmap.ic_video_load_default);
        helper.setText(R.id.tv_name,item.getName());
    }
}
