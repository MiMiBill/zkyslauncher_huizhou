package com.muju.note.launcher.app.orderfood.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.adapter.FoodPageAdapter;
import com.muju.note.launcher.app.orderfood.contract.OrderFoodContract;
import com.muju.note.launcher.app.orderfood.db.CommodityDao;
import com.muju.note.launcher.app.orderfood.event.OrderFoodEvent;
import com.muju.note.launcher.app.orderfood.event.OrderFoodRefreshEvent;
import com.muju.note.launcher.app.orderfood.presenter.OrderFoodPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//点餐服务
public class OrderFoodFragment extends BaseFragment<OrderFoodPresenter> implements OrderFoodContract.View{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_hos_name)
    TextView tvHosName;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.iv_shop)
    ImageView ivShop;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    private List<CommodityDao> foodCloumsBeanList = new ArrayList<>();
    private FoodPageAdapter pageAdapter;
    private double price=0.0;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_food;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderFoodEvent event) {
        price=event.getPrice();
        tvTotal.setText("￥ "+event.getPrice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderFoodRefreshEvent event) {
        LogFactory.l().i("event");
        mPresenter.queryTab();
    }

    @Override
    public void initData() {
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setText("点餐专区");
        ivImg.setImageResource(R.mipmap.food_shop_logo);
        tvHosName.setText(ActiveUtils.getPadActiveInfo().getHospitalName());

        mPresenter.queryTab();
    }


    private void setTab() {
        pageAdapter = new FoodPageAdapter(getChildFragmentManager(), foodCloumsBeanList);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 设置第一个tab选中
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        View view = LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout
                .tablayout_video_columns, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(tab.getText());
        tab.setCustomView(tvTitle);

        // 设置tablayout选中字体大小
        setTabLayoutTextSize();
    }


    /**
     * 设置tablayout字体大小
     */
    private void setTabLayoutTextSize() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = LayoutInflater.from(LauncherApplication.getContext()).inflate(R
                        .layout.tablayout_video_columns, null);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setText(tab.getText());
                tab.setCustomView(tvTitle);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new OrderFoodPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.ll_back, R.id.ll_null,R.id.tv_pay,R.id.tv_order})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_back:
            case R.id.ll_null:
                pop();
                break;
            case R.id.tv_pay:
                if(price==0){
                    ToastUtil.showToast(getActivity(),"请选择商品");
                    return;
                }
                start(new OrderPayFragment());
                break;
            case R.id.tv_order:
                start(new OrderListFragment());
                break;

        }
    }


    @Override
    public void getTab(List<CommodityDao> list) {
        foodCloumsBeanList.clear();
        foodCloumsBeanList.addAll(list);
        for (int i = 0; i < foodCloumsBeanList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(foodCloumsBeanList.get(i).getTitle()));
        }
        setTab();
    }

    @Override
    public void getTabNull() {
        llNull.setVisibility(View.VISIBLE);
    }


}
