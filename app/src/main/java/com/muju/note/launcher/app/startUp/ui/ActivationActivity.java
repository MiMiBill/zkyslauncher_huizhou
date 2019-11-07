package com.muju.note.launcher.app.startUp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.MainActivity;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.startUp.presenter.ActivationPresenter;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.util.toast.FancyToast;
import com.muju.note.launcher.util.toast.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

//激活页面
public class ActivationActivity extends BaseActivity<ActivationPresenter> implements
        ActivationPresenter.ActivationListener {
    @BindView(R.id.hide_btn)
    Button hideBtn;
    @BindView(R.id.tv_active_result)
    TextView tvActiveResult;
    @BindView(R.id.tv_tip_maintain)
    TextView tvTipMaintain;
    @BindView(R.id.tv_active_result_success)
    TextView tvActiveResultSuccess;
    @BindView(R.id.tv_active_result_fail)
    TextView tvActiveResultFail;
    @BindView(R.id.tv_tip_maintain_zk)
    TextView tvTipMaintainZk;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvImei)
    TextView tvImei;
    @BindView(R.id.iv_active_pad_qrcode)
    ImageView ivActivePadQrcode;
    @BindView(R.id.bt_next)
    Button btNext;
    AlertDialog dialog;
    EditText et;
    Disposable disposableCheckActive;
    Disposable disposableCheckNetWork;
    @BindView(R.id.lly_progress)
    LinearLayout llyProgress;
    @BindView(R.id.lly_active)
    LinearLayout llyActive;
    @BindView(R.id.tv_retry)
    TextView tvRetry;
    @BindView(R.id.lly_no_internet)
    LinearLayout llyNoInternet;
    @BindView(R.id.tv_reason)
    TextView tvReason;

    @Override
    public int getLayout() {
        return R.layout.activity_activation;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(ActivationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void initData() {
        mPresenter = new ActivationPresenter();
        mPresenter.setOnActivationListener(this);
        //版本不一样之后重置默认域名
        long versionCode = SPUtil.getLong(Constants.HOST_VERSION);
        if (versionCode != BuildConfig.VERSION_CODE) {
            SPUtil.putString(Constants.HOST, UrlUtil.HOST_DEFAULT);
            SPUtil.putLong(Constants.HOST_VERSION, BuildConfig.VERSION_CODE);
        }

        checkNetWork();
    }

    @Override
    public void initPresenter() {
        mPresenter = new ActivationPresenter();
    }


    private void checkNetWork() {
        boolean isReboot = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        String iccid = MobileInfoUtil.getICCID(this);

        if (MobileInfoUtil.haveSIMCard(this) && !TextUtils.isEmpty(iccid)) {
//            LogFactory.l().i("有手机卡");
            boolean isConnect = NetWorkUtil.isConnected(this);
            if (!isConnect) {
                llyNoInternet.setVisibility(View.VISIBLE);
                tvReason.setText("没有检测到网络,请重试");
                tvRetry.setText("重新加载网络");
                llyActive.setVisibility(View.GONE);
                llyProgress.setVisibility(View.GONE);
            } else {
                llyNoInternet.setVisibility(View.GONE);
                llyActive.setVisibility(View.VISIBLE);
                if (isReboot) {
                    loginHome();
                } else {
                    initHide();
                    SystemUtils.setVolumeNotification(this);
                    checkHadActiveDi();
                    setActiveLayout();
                }
            }
        } else {
//            LogFactory.l().i("没有手机卡");
            llyNoInternet.setVisibility(View.VISIBLE);
            tvReason.setText("没有检测到手机卡,请重试");
            tvRetry.setText("重新加载");
            llyActive.setVisibility(View.GONE);
            llyProgress.setVisibility(View.GONE);
        }
    }


    /**
     * 轮询查询是否激活
     */
    private void checkHadActiveDi() {
        checkHadActive();
        RxUtil.closeDisposable(disposableCheckActive);
        disposableCheckActive = Observable.interval(30, TimeUnit.SECONDS)
                .take(30)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        checkHadActive();
                    }
                });
    }

    /**
     * 检查设备是否激活
     */
    private void checkHadActive() {
        //重新请求服务器
        String iccid = MobileInfoUtil.getICCID(this);
        if (TextUtils.isEmpty(iccid)) {
            ToastUtil.showToast(this, "非法设备！");
            return;
        }

        mPresenter.bindingDevice(MobileInfoUtil.getIMEI(this));
    }

    //隐藏页面
    private void initHide() {
        et = new EditText(this);
        dialog = new AlertDialog.Builder(this)
                .setView(et)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (UIUtils.checkHidePwd(ActivationActivity.this, et.getText().toString
                                ())) {
                            HideActivity.launch(ActivationActivity.this);
                        } else {
                            FancyToast.makeText(ActivationActivity.this, "验证码错误", 0);
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                et.setText("");
            }
        });
        hideBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog.show();
                return true;
            }
        });
    }

    private void setActiveLayout() {
        //二维码
        ivActivePadQrcode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID(this)
                + "," + MobileInfoUtil.getIMEI(this), 232, 232));

        tvIccid.setText("iccId:" + MobileInfoUtil.getICCID(this));
        tvImei.setText("imei:" + MobileInfoUtil.getIMEI(this));

        Log.d("iccId:imei", MobileInfoUtil.getICCID(this) + "," + MobileInfoUtil.getIMEI(this));
        //提示
        //todo 不要点击，直接激活后就自动跳转
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHadActiveDi();
            }

        });
    }

    /**
     * 激活失败
     */
    private void activeFail() {
        llyProgress.setVisibility(View.GONE);
        tvActiveResult.setVisibility(View.INVISIBLE);
        tvTipMaintain.setVisibility(View.INVISIBLE);
        tvActiveResultSuccess.setVisibility(View.INVISIBLE);
        tvActiveResultFail.setVisibility(View.VISIBLE);
        tvTipMaintainZk.setVisibility(View.VISIBLE);
        ivActivePadQrcode.setVisibility(View.VISIBLE);
        tvTipMaintainZk.setText("请用运维工具扫码激活！");
        btNext.setText("重新激活");
    }

    /**
     * 激活成功
     */
    private void activeSuccess() {
        tvActiveResult.setVisibility(View.INVISIBLE);
        tvTipMaintain.setVisibility(View.INVISIBLE);
        tvActiveResultSuccess.setVisibility(View.VISIBLE);
        tvActiveResultFail.setVisibility(View.INVISIBLE);
        tvTipMaintainZk.setVisibility(View.VISIBLE);
        ivActivePadQrcode.setVisibility(View.INVISIBLE);
        tvTipMaintainZk.setText("辛苦了运维同事，感谢您的不辞辛劳！");
        btNext.setText("立即使用");
    }

    /**
     * 跳转到首页
     */
    private void loginHome() {
        llyProgress.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.closeDisposable(disposableCheckActive);
        RxUtil.closeDisposable(disposableCheckNetWork);
        handler.removeMessages(1);
    }


    @Override
    public void bindSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                if ((jsonObject.optString("data") != null) && (!jsonObject.optString("data")
                        .equals("[]"))) {
                    Gson gson = new Gson();
                    ActivePadInfo activePadInfo = gson.fromJson(response, ActivePadInfo.class);
                    ArrayList<ActivePadInfo.DataBean> data = activePadInfo.getData();
                    if (data != null && data.size() > 0) {
                        int activetion = activePadInfo.getData().get(0).getActivetion();
                        if (activetion == 0) {
                            //未激活
                            bindFail();
                        } else if (activetion == 1) {
                            //已激活
                            ActivePadInfo.DataBean dataBean = activePadInfo.getData().get(0);
                            dataBean.setHost(UrlUtil.getHost());
                            ActiveUtils.setActiveInfo(dataBean);
                            activeSuccess();
                            //添加统一header
                            HttpHeaders headers = new HttpHeaders();
                            headers.put("PAD", activePadInfo.getData().get(0).getPad());
                            OkGo.getInstance().addCommonHeaders(headers);
                            loginHome();

                            JPushInterface.setAlias(this, 2, dataBean.getBedId() + "pad"
                                    + dataBean.getActive());

                        }
                    } else {
                        //未激活
                        bindFail();
                    }
                } else {
                    bindFail();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindFail() {
        activeFail();
    }


    @OnClick(R.id.tv_retry)
    public void onViewClicked() {
        checkNetWork();
    }
}
