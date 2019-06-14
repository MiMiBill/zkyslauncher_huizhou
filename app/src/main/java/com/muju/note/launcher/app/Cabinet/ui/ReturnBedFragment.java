package com.muju.note.launcher.app.Cabinet.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.bean.CabinetBean;
import com.muju.note.launcher.app.Cabinet.contract.CabinetOrderContract;
import com.muju.note.launcher.app.Cabinet.event.OrderCloseEvent;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetOrderPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ArithUtil;
import com.muju.note.launcher.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 还柜状态
 */
public class ReturnBedFragment extends BaseFragment<CabinetOrderPresenter> implements CabinetOrderContract.View {
    private static String RETURNBED_CABINET_BEAN = "returnbed_cabinet_bean";
    private static String RETURNBED_TYPE = "returnbed_type";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.rel_status)
    RelativeLayout relStatus;
    @BindView(R.id.tv_rent_price)
    TextView tvRentPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_su)
    TextView tvSu;
    @BindView(R.id.btn_su)
    Button btnSu;
    @BindView(R.id.tv_rent)
    TextView tvRent;
    @BindView(R.id.lly_price)
    LinearLayout llyPrice;
    private CabinetBean.DataBean dataBean;
    private int type = 1; //表示成功  2表示失败

    public static ReturnBedFragment newInstance(int type, CabinetBean.DataBean dataBean) {
        Bundle args = new Bundle();
        args.putSerializable(RETURNBED_CABINET_BEAN, dataBean);
        args.putInt(RETURNBED_TYPE, type);
        ReturnBedFragment fragment = new ReturnBedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void unLock(String data) {

    }

    @Override
    public void unLockFail() {

    }

    @Override
    public void returnBedFail() {
        showToast("归还失败,请稍后再试");
    }

    @Override
    public void reTurnBed(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                type=1;
                setSuccessUi();
            } else {
               showToast("归还失败,请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_return_bed;
    }

    @Override
    public void initData() {
        dataBean = (CabinetBean.DataBean) getArguments().getSerializable(RETURNBED_CABINET_BEAN);
        type = getArguments().getInt(RETURNBED_TYPE);
        if(type==1){
            setSuccessUi();
        }else {
            setFailUi();
        }
    }

    //失败
    private void setFailUi() {
        tvStatus.setText("归还失败");
        relStatus.setVisibility(View.VISIBLE);
        llyPrice.setVisibility(View.GONE);
        tvSu.setText("归还失败了");
        tvSu.setVisibility(View.VISIBLE);
        btnSu.setText("重试");
    }

    //成功
    private void setSuccessUi() {
        tvStatus.setText("归还成功");
        relStatus.setVisibility(View.GONE);
        llyPrice.setVisibility(View.VISIBLE);
        tvSu.setVisibility(View.GONE);
        btnSu.setText("完成");
        long currentTime=System.currentTimeMillis()/1000;
        long formartTime = DateUtil.formartTime(dataBean.getLeaseTime());
        tvTime.setText("租用时间:"+DateUtil.getTime((int)(currentTime-formartTime)));
        tvRent.setText("归还成功,押金"+dataBean.getDeposit()+"元已退还到支付的账户中,请注意查收");
        tvRentPrice.setText("¥"+ArithUtil.add(dataBean.getDeposit(),dataBean.getPayPrice()));
    }

    @Override
    public void initPresenter() {
        mPresenter=new CabinetOrderPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.ll_back, R.id.btn_su})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                EventBus.getDefault().post(new OrderCloseEvent()); //关闭订单页
                break;
            case R.id.btn_su:
                if(type==1){
                    pop();
                    EventBus.getDefault().post(new OrderCloseEvent()); //关闭订单页
                }else {
                    mPresenter.returnBed(dataBean.getId());
                }
                break;
        }
    }
}
