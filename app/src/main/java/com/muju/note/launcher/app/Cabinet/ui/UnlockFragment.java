package com.muju.note.launcher.app.Cabinet.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.bean.CabinetBean;
import com.muju.note.launcher.app.Cabinet.bean.LockBean;
import com.muju.note.launcher.app.Cabinet.contract.CabinetOrderContract;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetOrderPresenter;
import com.muju.note.launcher.base.BaseFragment;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 屏安柜
 */
public class UnlockFragment extends BaseFragment<CabinetOrderPresenter> implements CabinetOrderContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.rel_status)
    RelativeLayout relStatus;
    @BindView(R.id.tv_su)
    TextView tvSu;
    @BindView(R.id.btn_su)
    Button btnSu;
    private static String LOCK_CABINET_BEAN="lock_cabinet_bean";
    private static String LOCK_TYPE="lock_type";
    private CabinetBean.DataBean dataBean;
    private int type=1; //表示成功  2表示失败
    public static UnlockFragment newInstance(int type,CabinetBean.DataBean dataBean) {
        Bundle args = new Bundle();
        args.putSerializable(LOCK_CABINET_BEAN, dataBean);
        args.putInt(LOCK_TYPE, type);
        UnlockFragment fragment = new UnlockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void unLock(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                LockBean lockBean = new Gson().fromJson(data, LockBean.class);
                if (lockBean != null && lockBean.getData()!=null && lockBean.getData().getCode()==200
                        && lockBean.getData().getObject()!=null && lockBean.getData().getObject().getCode()==200) {
                    type=1;
                    setSuccessUi();
                } else {
                    showToast("打开柜子失败,请稍后重试");
                }
            } else {
                showToast("打开柜子失败,请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLockFail() {

    }

    @Override
    public void returnBedFail() {

    }

    @Override
    public void reTurnBed(String data) {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_unlock;
    }

    @Override
    public void initData() {
        dataBean = (CabinetBean.DataBean) getArguments().getSerializable(LOCK_CABINET_BEAN);
        type = getArguments().getInt(LOCK_TYPE);
        if(type==1){
            setSuccessUi();
        }else {
            setFailUi();
        }
    }

    private void setSuccessUi() {
        relStatus.setBackground(getResources().getDrawable(R.mipmap.unlock_su_circle));
        ivStatus.setImageResource(R.mipmap.unlock_success);
        tvStatus.setText("开锁成功");
        tvSu.setText("医护床柜锁已打开,请安心使用!");
        btnSu.setText("完成");
    }

    private void setFailUi() {
        relStatus.setBackground(getResources().getDrawable(R.mipmap.unlock_fail_circle));
        ivStatus.setImageResource(R.mipmap.unlock_fail);
        tvStatus.setText("开锁失败");
        tvSu.setText("开锁失败了");
        btnSu.setText("重试");
    }

    @Override
    public void initPresenter() {
        mPresenter = new CabinetOrderPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.ll_back, R.id.btn_su})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                break;
            case R.id.btn_su:
                if(type==1){
                    pop();
                }else {
                    mPresenter.unLock(dataBean.getCabinetCode());
                }
                break;
        }
    }
}
