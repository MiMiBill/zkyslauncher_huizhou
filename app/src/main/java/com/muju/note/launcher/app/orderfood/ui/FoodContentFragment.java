package com.muju.note.launcher.app.orderfood.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.adapter.FoodContentAdapter;
import com.muju.note.launcher.app.orderfood.bean.ShopCart;
import com.muju.note.launcher.app.orderfood.contract.FoodContentContract;
import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.app.orderfood.event.OrderFoodEvent;
import com.muju.note.launcher.app.orderfood.imp.ShopCartImp;
import com.muju.note.launcher.app.orderfood.presenter.FoodContentPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 点餐列表
 */
public class FoodContentFragment extends BaseFragment<FoodContentPresenter> implements FoodContentContract.View,ShopCartImp {
    public static final String TAG = "FoodContentFragment";
    private static final String COLUMNS_ID = "commodid";
    @BindView(R.id.rv_video_view)
    RecyclerView rvVideoView;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private int columnsId=0;
    private int pageNum = 1;
    private FoodContentAdapter contentAdapter;
    private ArrayList<ComfoodDao> foodList=new ArrayList<>();//数据源
    public static ShopCart shopCart;

    public static FoodContentFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(COLUMNS_ID, id);
        FoodContentFragment fragment = new FoodContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_food_content;
    }

    @Override
    public void initData() {
        shopCart = new ShopCart();
        Bundle args = getArguments();
        if (args != null) {
            columnsId = args.getInt(COLUMNS_ID);
        }

        mPresenter.queryFoodList(1,columnsId);
        refreshFoodList();
    }


    @Override
    public void initPresenter() {
        mPresenter=new FoodContentPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @Override
    public void getFoodList(List<ComfoodDao> list) {
        smartRefresh.finishRefresh();
        foodList.clear();
        foodList.addAll(list);
        contentAdapter = new FoodContentAdapter(getActivity(),foodList,shopCart);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(LauncherApplication.getContext(),4);
        rvVideoView.setLayoutManager(gridLayoutManager);
        rvVideoView.setAdapter(contentAdapter);
        contentAdapter.setShopCartImp(this);
    }

    @Override
    public void getListNull() {
        smartRefresh.finishRefresh();
    }

    @Override
    public void getListEnd() {
        smartRefresh.finishRefresh();
    }

    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
    }


    private void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>=0){
            EventBus.getDefault().post(new OrderFoodEvent(shopCart.getShoppingTotalPrice()));
        }else {
            LogFactory.l().i("shopCart==null");
            EventBus.getDefault().post(new OrderFoodEvent(0.0));
        }
    }


    private void refreshFoodList() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum=1;
                mPresenter.queryFoodList(1,columnsId);
            }
        });
    }

}
