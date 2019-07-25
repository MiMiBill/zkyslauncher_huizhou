package com.muju.note.launcher.app.orderfood.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.orderfood.contract.PayContract;
import com.muju.note.launcher.app.orderfood.event.OrderFoodCloseEvent;
import com.muju.note.launcher.app.orderfood.event.OrderFoodRefreshEvent;
import com.muju.note.launcher.app.orderfood.presenter.PayPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class PayFragment extends BaseFragment<PayPresenter> implements PayContract.View {
    public static final String TAG = "FoodContentFragment";
    private static final String ORDER_ID = "orderId";
    private static final String ORDER_CODE = "orderCode";
    private static final String ORDER_PRICE = "order_price";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.lly_no_pay)
    LinearLayout llyNoPay;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.lly_status)
    LinearLayout llyStatus;
    private int orderId = 0;
    private String imgCode = "";
    private double price = 0.0;
    /**
     * 轮询查询用户支付信息,8s一次
     */
    Disposable disposableSlPay;
    private CountDownTimer countDownTimer;

    public static PayFragment newInstance(int id, String code, double price) {
        Bundle args = new Bundle();
        args.putInt(ORDER_ID, id);
        args.putString(ORDER_CODE, code);
        args.putDouble(ORDER_PRICE, price);
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_pay;
    }

    @Override
    public void initData() {
        LogFactory.l().i("initData");
        tvTitle.setText("支付订单");
        Bundle args = getArguments();
        if (args != null) {
            orderId = args.getInt(ORDER_ID);
            imgCode = args.getString(ORDER_CODE);
            price = args.getDouble(ORDER_PRICE);
        }
        tvPrice.setText("￥" + price);
        tvHospital.setText(ActiveUtils.getPadActiveInfo().getHospitalName());
        ivCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(imgCode, 212, 212));

        queryPay();
    }


    private void queryPay() {
        RxUtil.closeDisposable(disposableSlPay);

        disposableSlPay = Observable.interval(0, 8, TimeUnit.SECONDS)
                .take(75)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mPresenter.queryPay(orderId);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Looper.prepare();
                        showToast("支付超时");
                        Looper.loop();
                        canclePay();
                    }
                });
        setTime();
    }


    private void canclePay() {
        RxUtil.closeDisposable(disposableSlPay);
        llyNoPay.setVisibility(View.GONE);
        llyStatus.setVisibility(View.VISIBLE);
        ivStatus.setImageResource(R.mipmap.order_fail);
        mPresenter.canclePay(orderId);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PayPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        EventBus.getDefault().post(new OrderFoodRefreshEvent());
        EventBus.getDefault().post(new OrderFoodCloseEvent());
        pop();
    }

    @Override
    public void queryPay(String data) {
//        RxUtil.closeDisposable(disposableSlPay);
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                if (jsonObject.optInt("data") == 0) {   //未支付
//                    mPresenter.queryPay(orderId);
                } else if (jsonObject.optInt("data") == 1) { //支付成功
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    RxUtil.closeDisposable(disposableSlPay);
                    llyNoPay.setVisibility(View.GONE);
                    llyStatus.setVisibility(View.VISIBLE);
                    ivStatus.setImageResource(R.mipmap.order_success);
                    tvStatus.setText("支付成功");
                } else if (jsonObject.optInt("data") == 5) { //支付失败
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    tvStatus.setText("支付失败");
                    canclePay();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void canclePay(String data) {

    }


    private void setTime() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        countDownTimer = new CountDownTimer(600*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvTime != null && millisUntilFinished > 0) {
                    String time="支付剩余时间: "+DateUtil.diffTime(millisUntilFinished);
                    setTextTime(time);
                }
            }

            @Override
            public void onFinish() {
                tvStatus.setText("支付超时");
                canclePay();
            }
        };
        countDownTimer.start();
    }


    private void setTextTime(String time){
        SpannableStringBuilder sb=new SpannableStringBuilder(time);
        sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_585858)),0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_FF3000)),8,time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(1.25f),8,time.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTime.setText(sb);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        RxUtil.closeDisposable(disposableSlPay);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
