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
import com.muju.note.launcher.app.Cabinet.bean.LockInfo;
import com.muju.note.launcher.app.Cabinet.contract.CabinetOrderContract;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetOrderPresenter;
import com.muju.note.launcher.base.BaseFragment;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 屏安柜开锁状态
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
    private static String LOCK_REASON="lock_reason";
    private CabinetBean.DataBean dataBean;
    private int type=1; //查询锁状态  2:网络失败
    private String reason="";
    private boolean isLock=false;
    public static UnlockFragment newInstance(int type,CabinetBean.DataBean dataBean,String reason) {
        Bundle args = new Bundle();
        args.putSerializable(LOCK_CABINET_BEAN, dataBean);
        args.putInt(LOCK_TYPE, type);
        args.putString(LOCK_REASON, reason);
        UnlockFragment fragment = new UnlockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void unLock(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                if(jsonObject.optString("data")!=null){
                    String objData=jsonObject.optString("data");
                    JSONObject obj=new JSONObject(objData);
                    if(obj.optInt("code")==200){
                        if (obj.optString("object").equals("ok")){
                            mPresenter.findByDid(dataBean.getCabinetCode());
                        }else {
                            String object = obj.optString("object");
                            JSONObject lockObj=new JSONObject(object);
                            if(lockObj!=null){
                                if(lockObj.optInt("code")==200){
                                    mPresenter.findByDid(dataBean.getCabinetCode());
                                }else {
                                    setFailUi(lockObj.optString("msg"));
                                }
                            }else {
                                setFailUi("连接第三方服务器异常");
                            }
                        }
                    }else {
                        setFailUi("连接第三方服务器异常");
                    }
                }
            }else {
                setFailUi("服务器异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLockFail() {
        isLock=false;
        setFailUi("网络错误");
    }

    @Override
    public void returnBedFail() {

    }

    @Override
    public void reTurnBed(String data) {

    }

    @Override
    public void findByDid(String data) {
        try {
            JSONObject jsonObject=new JSONObject(data);
            if(jsonObject.optString("data")!=null){
                String optString = jsonObject.optString("data");
                Gson gson=new Gson();
                LockInfo lockInfo = gson.fromJson(optString, LockInfo.class);
                if(lockInfo.getLockStatus()==2){  //开锁成功
                    setSuccessUi();
                    isLock=true;
                }else {
                    isLock=false;
                    setFailUi("第三方开锁失败");
                }
            }else {
                isLock=false;
                setFailUi("第三方服务器异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_unlock;
    }

    @Override
    public void initData() {
        dataBean = (CabinetBean.DataBean) getArguments().getSerializable(LOCK_CABINET_BEAN);
        type = getArguments().getInt(LOCK_TYPE);
        reason = getArguments().getString(LOCK_REASON);

        if(type==1){
            mPresenter.findByDid(dataBean.getCabinetCode());
        }else {
            setFailUi(reason);
        }
    }

    private void setSuccessUi() {
        relStatus.setBackground(getResources().getDrawable(R.mipmap.unlock_su_circle));
        ivStatus.setImageResource(R.mipmap.unlock_success);
        tvStatus.setText("开锁成功");
        tvSu.setText("医护床柜锁已打开,请安心使用!");
        btnSu.setText("完成");
        btnSu.setVisibility(View.VISIBLE);
    }

    private void setFailUi(String reason) {
        relStatus.setBackground(getResources().getDrawable(R.mipmap.unlock_fail_circle));
        ivStatus.setImageResource(R.mipmap.unlock_fail);
        tvStatus.setText("开锁失败");
        tvSu.setText("开锁失败了-"+reason);
        btnSu.setText("重试");
        btnSu.setVisibility(View.VISIBLE);
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
                if(isLock){
                    pop();
                }else {
                    mPresenter.unLock(dataBean.getCabinetCode());
                }
                break;
        }
    }
}
