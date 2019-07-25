package com.muju.note.launcher.app.orderfood.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.dialog.AddressPopupWindow;
import com.muju.note.launcher.app.orderfood.adapter.FoodOrderAdapter;
import com.muju.note.launcher.app.orderfood.bean.PayInfoBean;
import com.muju.note.launcher.app.orderfood.contract.OrderPayContract;
import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.app.orderfood.event.OrderFoodCloseEvent;
import com.muju.note.launcher.app.orderfood.presenter.OrderPayPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.ClickTimeUtils;
import com.muju.note.launcher.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.muju.note.launcher.app.orderfood.ui.FoodContentFragment.shopCart;
//订单提交页面
public class OrderPayFragment extends BaseFragment<OrderPayPresenter> implements OrderPayContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.lly_phone)
    LinearLayout llyPhone;
    @BindView(R.id.lly_address)
    LinearLayout llyAddress;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.edt_desc)
    EditText edtDesc;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    private FoodOrderAdapter foodOrderAdapter;
    ArrayList<ComfoodDao> comfoodDaos=new ArrayList<>();
    private ActivePadInfo.DataBean activeInfo;
    private String orderName="";
    private String orderPhone="";
    private String remark="";
    private AddressPopupWindow addressPopupWindow;

    @Override
    public int getLayout() {
        return R.layout.fragment_order_pay;
    }

    @Override
    public void initData() {
        tvTitle.setText("提交订单");
        activeInfo = ActiveUtils.getPadActiveInfo();
        comfoodDaos.addAll(shopCart.getShoppingSingleMap().keySet());


        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        foodOrderAdapter = new FoodOrderAdapter(comfoodDaos,shopCart);
        recyclerview.setAdapter(foodOrderAdapter);

        tvAddress.setText(activeInfo.getHospitalName()+activeInfo.getDeptName()+activeInfo.getBedNumber()+"床");
        tvPrice.setText("合计: ￥ "+shopCart.getShoppingTotalPrice());
    }

    @Override
    public void initPresenter() {
        mPresenter=new OrderPayPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    @OnClick({R.id.ll_back, R.id.lly_phone, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                break;
            case R.id.lly_phone:
                if (addressPopupWindow != null) {
                    addressPopupWindow.dismiss();
                    addressPopupWindow = null;
                }else {
                    showPop();
                }
                break;
            case R.id.tv_pay:
                if(ClickTimeUtils.isFastDoubleClick())
                    return;
                topay();
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderFoodCloseEvent event) {
        pop();
    }


    private void topay() {
        if(orderName.equals("") || orderPhone.equals("")){
            ToastUtil.showToast(getActivity(),"请填写收货人");
            return;
        }
        remark=edtDesc.getText().toString().trim();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("sellerId",1);
            jsonObject.put("remark",remark);
            jsonObject.put("totalAmount",shopCart.getShoppingTotalPrice());
            jsonObject.put("payType",3);
            jsonObject.put("type",3);
            jsonObject.put("bedId",activeInfo.getBedId());
            jsonObject.put("name",orderName);
            jsonObject.put("mobile",orderPhone);

            JSONArray jsonArray=new JSONArray();
            for (int i = 0; i < comfoodDaos.size(); i++) {
                JSONObject obj=new JSONObject();
                obj.put("commodityId",comfoodDaos.get(i).getFoodid()+"");
                obj.put("count",shopCart.getShoppingSingleMap().get(comfoodDaos.get(i)));
                jsonArray.put(obj);
            }
            jsonObject.put("items",jsonArray);
//            LogFactory.l().i("jsonobject==="+jsonObject.toString());
            mPresenter.orderCreate(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    //地址输入
    private void showPop() {
        addressPopupWindow = new AddressPopupWindow(getActivity());
        addressPopupWindow.showPopupWindow(llyAddress);
        addressPopupWindow.setOnSureClickListener(new AddressPopupWindow.SureClickListener() {
            @Override
            public void onSureClick(String name, String phoneNum) {
                orderName=name;
                orderPhone=phoneNum;
                tvPhone.setText("姓名:"+orderName+"     手机号码:"+orderPhone);
                addressPopupWindow.dismiss();
            }
        });
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (addressPopupWindow != null) {
            addressPopupWindow.dismiss();
            addressPopupWindow = null;
        }
        hideSoftInput();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void orderCreate(String data) {
//        LogFactory.l().i("data==="+data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.optInt("code")==200){
                Gson gson=new Gson();
                PayInfoBean payInfoBean = gson.fromJson(data, PayInfoBean.class);
                PayInfoBean.DataBean dataBean = payInfoBean.getData();
                start(PayFragment.newInstance(dataBean.getOrderId(),dataBean.getQrCode(),
                        shopCart.getShoppingTotalPrice()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void orderError() {
        ToastUtil.showToast(getActivity(),"订单提交失败,请稍后重试");
    }
}
