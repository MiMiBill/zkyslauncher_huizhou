package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;

import java.util.List;

public class HospitalMienAdapter extends BaseQuickAdapter<MienInfoDao, BaseViewHolder> {

    public HospitalMienAdapter(int layoutResId, @Nullable List<MienInfoDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MienInfoDao item) {
        TextView tvType = helper.getView(R.id.tv_type);
        tvType.setText(item.getTitle());
        if (pos == helper.getAdapterPosition()) {
            tvType.setSelected(true);
        } else {
            tvType.setSelected(false);
        }
    }

    private int pos = 0;

    public void setPos(int pos) {
        this.pos = pos;
    }
}
