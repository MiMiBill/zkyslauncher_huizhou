package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/8.
 */

public class SearchAdapter extends BaseQuickAdapter<InfomationDao,SearchAdapter.SpinerHolder> {
    private List<InfomationDao> spinerList;
    private Context context;
    public OnItemClickListener mItemClickListener;
    public SearchAdapter(Context context, ArrayList<InfomationDao> data) {
        super(R.layout.spiner_window_layout_item,data);
        this.context=context;
        this.spinerList = new ArrayList<>();
        if (data != null) {
            this.spinerList.addAll(data);
        }
    }

    public void nodfiyData(ArrayList<InfomationDao> list) {
        if (list != null) {
            this.spinerList.clear();
            this.spinerList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    protected void convert(SpinerHolder helper, InfomationDao item) {

        helper.setText(R.id.item,item.getTitle()) ;
    }


    public  interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

    public class SpinerHolder extends BaseViewHolder {
        public SpinerHolder(View view) {
            super(view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mItemClickListener.onItemClick(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
