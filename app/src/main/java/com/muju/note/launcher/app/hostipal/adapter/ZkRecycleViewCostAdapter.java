package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.CostInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class ZkRecycleViewCostAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CostInfo> mList;

    public ZkRecycleViewCostAdapter(Context context, List<CostInfo> list){
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new OneViewHolder(View.inflate(mContext, R.layout.recycle_item_cost_body, null));
        return new OneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycle_item_cost_body, parent, false));
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OneViewHolder oneViewHolder = (OneViewHolder) holder;
        oneViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class OneViewHolder extends RecyclerView.ViewHolder{
        private final TextView entryName;
        private final TextView number;
        private final TextView unit;
        private final TextView unitPrice;
        private final TextView totlePrice;
        private final TextView dateTime;

        public OneViewHolder(View view){
            super(view);
            entryName = view.findViewById(R.id.tv_enteyName);
            number = view.findViewById(R.id.tv_number);
            unit = view.findViewById(R.id.tv_unit);
            unitPrice = view.findViewById(R.id.tv_unitPrice);
            totlePrice = view.findViewById(R.id.tv_totlePrice);
            dateTime = view.findViewById(R.id.tv_dateTime);
        }

        public void setData(int position){
            entryName.setText(mList.get(position).getEntryName());
            number.setText(mList.get(position).getNumber());
            unit.setText(mList.get(position).getUnit());
            unitPrice.setText(mList.get(position).getUnitPrice());
            totlePrice.setText(mList.get(position).getTotlePrice());
            dateTime.setText(mList.get(position).getDateTime());
        }
    }
}
