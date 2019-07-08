package com.muju.note.launcher.app.video.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.bean.PriceBean;

import java.util.ArrayList;
import java.util.List;

public class VideoPriceAdapter extends BaseQuickAdapter<PriceBean.DataBean,VideoPriceAdapter.PriceViewHolder> {
    private Context context;
    private int selectIndex=-1;
    private List<PriceBean.DataBean> priceList =new ArrayList<>();
    private OnClickListener listener;
    public VideoPriceAdapter(Context context, @Nullable List<PriceBean.DataBean> data) {
        super(R.layout.video_item_price, data);
        this.context=context;
        if(data!=null){
            priceList.addAll(data);
        }
    }

    @Override
    protected void convert(VideoPriceAdapter.PriceViewHolder helper, PriceBean.DataBean item) {
        if(item.isCheck()){
            helper.tvPrice.setBackgroundResource(R.drawable.pay_price_5_select_bg);
            helper.tvPrice.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            helper.tvPrice.setBackgroundResource(R.drawable.pay_price_5_bg);
            helper.tvPrice.setTextColor(context.getResources().getColor(R.color.black));
        }
        if(item.getUnit()==1){
            helper.tvPrice.setText(item.getPrice()+"元/"+item.getAmount()+"小时");
        }else {
            helper.tvPrice.setText(item.getPrice()+"元/"+item.getAmount()+"天");
        }
    }


    public void notifyPrice(List<PriceBean.DataBean> list){
        if (list != null) {
            this.priceList.clear();
            this.priceList.addAll(list);
        }
        notifyDataSetChanged();
    }


    class PriceViewHolder extends BaseViewHolder{
        TextView tvPrice;
        public PriceViewHolder(View view) {
            super(view);
            tvPrice =(TextView)itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectIndex=getAdapterPosition();
                    if(listener!=null)
                        listener.onItemClick(selectIndex);
                }
            });
        }
    }


    public interface OnClickListener{
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnClickListener listener){
        this.listener=listener;
    }
}
