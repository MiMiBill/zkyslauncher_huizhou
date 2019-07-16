package com.muju.note.launcher.app.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.util.gilde.GlideUtil;

import java.util.List;

public class HomeHisVideoAdapter extends BaseQuickAdapter<VideoHisDao, BaseViewHolder> {
    public HomeHisVideoAdapter(int layoutResId, @Nullable List<VideoHisDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoHisDao item) {
        helper.setText(R.id.tv_name, item.getName());
        GlideUtil.loadImg(item.getScreenUrl(), (ImageView) helper.getView(R.id.iv_img), R.mipmap.ic_video_load_default);
    }
}
