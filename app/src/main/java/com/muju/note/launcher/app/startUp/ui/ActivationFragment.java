package com.muju.note.launcher.app.startUp.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.muju.note.launcher.BuildConfig;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.startUp.contract.NewActivationContract;
import com.muju.note.launcher.app.startUp.presenter.NewActivationPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 激活页面
 */
public class ActivationFragment extends BaseFragment<NewActivationPresenter> implements
        NewActivationContract.View {
    @BindView(R.id.iv_hide)
    ImageView ivHide;
    @BindView(R.id.tv_active_result)
    TextView tvActiveResult;
    @BindView(R.id.tv_tip_maintain)
    TextView tvTipMaintain;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvImei)
    TextView tvImei;
    @BindView(R.id.iv_active_pad_qrcode)
    ImageView ivActivePadQrcode;
    @BindView(R.id.lly_active)
    LinearLayout llyActive;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_active)
    Button btnActive;

    private Disposable disposableCheckActive;

    @Override
    public int getLayout() {
        return R.layout.avtivity_new_activation;
    }

    @Override
    public void initData() {

        tvIccid.setText("iccId:" + MobileInfoUtil.getICCID(LauncherApplication.getContext()));
        tvImei.setText("imei:" + MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        ivActivePadQrcode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID
                (LauncherApplication.getContext())
                + "," + MobileInfoUtil.getIMEI(LauncherApplication.getContext()), 232, 232));

        boolean isReboot = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        if (isReboot) {
            // 自启动，只做数据检查
            start(new StartCheckDataFragment());
        } else {
            // 手动启动，需要检查激活
            mPresenter.bindingDevice(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        }

    }

    @Override
    public void initPresenter() {
        mPresenter = new NewActivationPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void bindSuccess(ActivePadInfo.DataBean bean) {
        RxUtil.closeDisposable(disposableCheckActive);
        bean.setHost(UrlUtil.getHost());
        ActiveUtils.setActiveInfo(bean);
        //添加统一header
        HttpHeaders headers = new HttpHeaders();
        headers.put("PAD", bean.getPad());
        OkGo.getInstance().addCommonHeaders(headers);
        JPushInterface.setAlias(LauncherApplication.getContext(), 2, bean.getBedId() + "pad"
                + bean.getActive());
        tvActiveResult.setText("PAD已激活成功");
        tvTipMaintain.setText("辛苦了运维同事，感谢您的不辞辛劳！");
        tvActiveResult.setTextColor(Color.GREEN);
        start(new StartCheckDataFragment(), ISupportFragment.SINGLETASK);
    }

    @Override
    public void bindFail() {
        tvVersion.setText(String.format("版本:宝屏V%s", TextUtils.equals(UrlUtil.getHost(), "http://test" +
                ".pad.zgzkys.com") ? BuildConfig.VERSION_NAME + "beta" : BuildConfig.VERSION_NAME));
        RxUtil.closeDisposable(disposableCheckActive);
        disposableCheckActive = Observable.interval(30, TimeUnit.SECONDS)
                .take(1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mPresenter.bindingDevice(MobileInfoUtil.getIMEI(LauncherApplication
                                .getContext()));
                    }
                });
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        RxUtil.closeDisposable(disposableCheckActive);
    }

    @OnClick(R.id.btn_active)
    public void onViewClicked() {
        mPresenter.bindingDevice(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
    }
}
