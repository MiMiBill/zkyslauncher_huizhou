package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.AdvanceRecordInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class ZkRecycleViewAdvanceAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AdvanceRecordInfo> mList;

    public ZkRecycleViewAdvanceAdapter(Context context, List<AdvanceRecordInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new OneViewHolder(View.inflate(mContext, R.layout.recycle_item_advance_body, null));
        return new OneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycle_item_advance_body, parent, false));
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
    class OneViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvDateTime;
        private final TextView mTvAmount;
        private final TextView mTvAmountb;
        private final TextView mTvPayMethod;
        private final TextView mTvCashier;

        public OneViewHolder(View view) {
            super(view);
            mTvDateTime = view.findViewById(R.id.tv_date_time);
            mTvAmount = view.findViewById(R.id.tv_amount);
            mTvAmountb = view.findViewById(R.id.tv_amountb);
            mTvPayMethod = view.findViewById(R.id.tv_pay_method);
            mTvCashier = view.findViewById(R.id.tv_cashier);
        }

        public void setData(int position) {
            mTvDateTime.setText(mList.get(position).getDateTime());
            mTvAmount.setText(mList.get(position).getAmount());
            mTvAmountb.setText(mList.get(position).getAmountb());
            mTvPayMethod.setText(mList.get(position).getPayMethod());
            mTvCashier.setText(mList.get(position).getCashier());
        }
    }
}
