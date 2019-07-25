package com.muju.note.launcher.app.orderfood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.muju.note.launcher.app.orderfood.imp.ShopCartImp;

import java.util.ArrayList;

public class FoodContentAdapter  extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ComfoodDao> mMenuList=new ArrayList<>();
    private int mItemCount;
    private ShopCart shopCart;
    private ShopCartImp shopCartImp;


    public FoodContentAdapter(Context mContext, ArrayList<ComfoodDao> mMenuList, ShopCart shopCart) {
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.shopCart = shopCart;
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_food_content, viewGroup, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final DishViewHolder dishholder = (DishViewHolder) holder;
        if (dishholder != null) {

            final ComfoodDao dish = mMenuList.get(position);
            dishholder.name.setText(dish.getName());
            dishholder.price.setText("￥"+dish.getPrice());
            Glide.with(mContext).load(dish.getImages()).into(dishholder.iv_img);
            int count = 0;
            if (shopCart.getShoppingSingleMap().containsKey(dish)) {
                count = shopCart.getShoppingSingleMap().get(dish);
            }
            if (count <= 0) {
                dishholder.right_dish_remove_iv.setVisibility(View.GONE);
                dishholder.right_dish_account_tv.setVisibility(View.GONE);
            } else {
                dishholder.right_dish_remove_iv.setVisibility(View.VISIBLE);
                dishholder.right_dish_account_tv.setVisibility(View.VISIBLE);
                dishholder.right_dish_account_tv.setText(count + "");
            }
            dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shopCart.addShoppingSingle(dish)) {
                        notifyItemChanged(position);
                        if (shopCartImp != null)
                            shopCartImp.add(view, position);
                    }
                }
            });

            dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shopCart.subShoppingSingle(dish)) {
                        notifyItemChanged(position);
                        if (shopCartImp != null)
                            shopCartImp.remove(view, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }



    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price;
        private ImageView right_dish_remove_iv;
        private ImageView right_dish_add_iv;
        private ImageView iv_img;
        private TextView right_dish_account_tv;

        public DishViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            price = (TextView)itemView.findViewById(R.id.tv_price);
            iv_img = (ImageView)itemView.findViewById(R.id.iv_img);
            right_dish_remove_iv = (ImageView)itemView.findViewById(R.id.right_dish_remove);
            right_dish_add_iv = (ImageView)itemView.findViewById(R.id.right_dish_add);
            right_dish_account_tv = (TextView) itemView.findViewById(R.id.right_dish_account);
        }
    }
}
