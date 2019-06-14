package com.muju.note.launcher.app.Cabinet.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.bean.CabinetBean;
import com.muju.note.launcher.app.Cabinet.bean.LockBean;
import com.muju.note.launcher.app.Cabinet.contract.CabinetOrderContract;
import com.muju.note.launcher.app.Cabinet.event.OrderCloseEvent;
import com.muju.note.launcher.app.Cabinet.event.ReturnBedEvent;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetOrderPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.log.LogFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 屏安柜
 */
public class CabinetOrderFragment extends BaseFragment<CabinetOrderPresenter> implements CabinetOrderContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_open)
    TextView tvOpen;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.iv_normal)
    ImageView ivNormal;
    @BindView(R.id.rel_exception)
    RelativeLayout relException;
    private static final String CABINET_BEAN="cabinet_bean";
    private CabinetBean.DataBean dataBean;
    public static CabinetOrderFragment newInstance(CabinetBean.DataBean dataBean) {
        Bundle args = new Bundle();
        args.putSerializable(CABINET_BEAN, dataBean);
        CabinetOrderFragment fragment = new CabinetOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_cabinet_order;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        dataBean = (CabinetBean.DataBean) getArguments().getSerializable(CABINET_BEAN);
        if(dataBean!=null){
            tvStart.setText(dataBean.getLeaseTime());
            tvEnd.setText(dataBean.getExpireTime());
            if(dataBean.getStatus()==2){    //正在使用
                tvStatus.setText("正在使用中");
                ivNormal.setVisibility(View.VISIBLE);
                relException.setVisibility(View.GONE);
                tvStatus.setTextColor(getResources().getColor(R.color.color_333333));
            }else {     //逾期使用
                tvStatus.setText("逾期使用中");
                ivNormal.setVisibility(View.GONE);
                relException.setVisibility(View.VISIBLE);
                tvStatus.setTextColor(getResources().getColor(R.color.color_FF6339));

            }
            setTime();
        }
    }




    //推送成功或者订单页面归还成功,重新拉取订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderCloseEvent event) {
        EventBus.getDefault().post(new ReturnBedEvent());
        pop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogFactory.l().i("onDestroy");
        EventBus.getDefault().unregister(this);
    }

    //设置时间显示
    private void setTime() {
        long cuTime = System.currentTimeMillis()/1000;
        long lastTime=DateUtil.formartTime(dataBean.getExpireTime());
        tvDay.setText(DateUtil.getDay((int)(lastTime-cuTime)));
        tvHour.setText(DateUtil.getHour((int)(lastTime-cuTime)));
    }

    @Override
    public void initPresenter() {
        mPresenter=new CabinetOrderPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @OnClick({R.id.ll_back, R.id.tv_back, R.id.tv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                break;
            case R.id.tv_back:
                mPresenter.returnBed(dataBean.getId());
                break;
            case R.id.tv_open:
                mPresenter.unLock(dataBean.getCabinetCode());
                break;
        }
    }

    @Override
    public void unLock(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                LockBean lockBean = new Gson().fromJson(data, LockBean.class);
                if (lockBean != null && lockBean.getData()!=null && lockBean.getData().getCode()==200
                        && lockBean.getData().getObject()!=null && lockBean.getData().getObject().getCode()==200) {
                    start(UnlockFragment.newInstance(1,dataBean));
                } else {
                    start(UnlockFragment.newInstance(2,dataBean));
                }
            } else {
                start(UnlockFragment.newInstance(2,dataBean));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLockFail() {
        start(UnlockFragment.newInstance(2,dataBean));
    }

    @Override
    public void returnBedFail() {
        start(ReturnBedFragment.newInstance(2,dataBean));
    }

    @Override
    public void reTurnBed(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                start(ReturnBedFragment.newInstance(1,dataBean));
            } else {
                start(ReturnBedFragment.newInstance(2,dataBean));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
