package com.muju.note.launcher.app.orderfood.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.bean.ShopCart;
import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderAdapter extends RecyclerView.Adapter {
    private ShopCart shopCart;
    private List<ComfoodDao> data = new ArrayList<>();
    public FoodOrderAdapter(@Nullable List<ComfoodDao> data,ShopCart shopCart) {
        this.data=data;
        this.shopCart=shopCart;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout.item_food_order, viewGroup, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DishViewHolder dishholder = (DishViewHolder)viewHolder;
        ComfoodDao comfoodDao = data.get(position);
        if(comfoodDao!=null){
            int num = shopCart.getShoppingSingleMap().get(comfoodDao);
            dishholder.tv_name.setText(comfoodDao.getName());
            dishholder.tv_price.setText(comfoodDao.getPrice()+"");
            Glide.with(LauncherApplication.getContext()).load(comfoodDao.getImages()).into(dishholder.iv_img);
            dishholder.tv_num.setText("X"+num);
        }
    }




    @Override
    public int getItemCount() {
        return data.size();
    }

    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_num,tv_price;
        private ImageView iv_img;


        public DishViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView)itemView.findViewById(R.id.iv_img);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_num = (TextView)itemView.findViewById(R.id.tv_num);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
        }
    }
}
