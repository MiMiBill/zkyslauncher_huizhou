package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.bean.ChemotherapayItemInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class ChemotherapayFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChemotherapayItemInfo> mList;

    public ChemotherapayFragmentAdapter(Context context, List<ChemotherapayItemInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.chemotherapay_fragment_rcl_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvProjectName;
        private final TextView mTvResult;
        private final TextView mTvUnit;
        private final TextView mTvTip;
        private final TextView mTvReRange;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvProjectName = itemView.findViewById(R.id.tv_project_name);
            mTvResult = itemView.findViewById(R.id.tv_result);
            mTvUnit = itemView.findViewById(R.id.tv_unit);
            mTvTip = itemView.findViewById(R.id.tv_tip);
            mTvReRange = itemView.findViewById(R.id.tv_reference_range);
        }

        //数据显示
        public void setData(int position) {
            mTvProjectName.setText(mList.get(position).getProjectNmae());
            mTvResult.setText(mList.get(position).getResult());
            mTvUnit.setText(mList.get(position).getUnit());
            mTvTip.setText(mList.get(position).getTip());
            mTvReRange.setText(mList.get(position).getReRange());
        }
    }
}
