package com.muju.note.launcher.app.hostipal.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.DepartmentInfoDao;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;

import java.util.List;

public class HospitalMienTabAdapter extends BaseQuickAdapter<DepartmentInfoDao, BaseViewHolder> {

    public HospitalMienTabAdapter(int layoutResId, @Nullable List<DepartmentInfoDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartmentInfoDao item) {
        TextView tvType = helper.getView(R.id.tv_type);
        tvType.setText(item.getDeptName());
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
