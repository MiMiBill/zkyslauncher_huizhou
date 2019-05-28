package com.muju.note.launcher.app.video.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;

import java.util.List;

public class VideoLineAdapter extends BaseQuickAdapter<VideoInfoDao,BaseViewHolder> {

    public VideoLineAdapter(int layoutResId, @Nullable List<VideoInfoDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoInfoDao item) {
        helper.setText(R.id.tv_name,item.getName());
        ImageView ivPlaying=helper.getView(R.id.iv_playing);
        if(pos==helper.getAdapterPosition()){
            ivPlaying.setVisibility(View.VISIBLE);
        }else {
            ivPlaying.setVisibility(View.GONE);
        }
    }

    private int pos=0;
    public void setPos(int pos) {
        this.pos = pos;
    }
}
