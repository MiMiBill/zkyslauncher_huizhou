package com.muju.note.launcher.app.sign.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.sign.bean.PriseBean;

import java.util.List;

public class PriseAdapter extends BaseQuickAdapter<PriseBean, BaseViewHolder> {

    public PriseAdapter(int layoutResId, @Nullable List<PriseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PriseBean item) {
//        GlideUtil.loadImg(item.getImg(), (ImageView) helper.getView(R.id.iv_img),R.mipmap.ic_video_load_default);
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_count, item.getCount()+"");
    }
}
