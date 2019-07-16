package com.muju.note.launcher.app.startUp.ui;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ActivePadInfo;
import com.muju.note.launcher.app.startUp.CheckMsgEvent;
import com.muju.note.launcher.app.startUp.adapter.ActivationCheckAdapter;
import com.muju.note.launcher.app.startUp.contract.NewActivationContract;
import com.muju.note.launcher.app.startUp.presenter.NewActivationPresenter;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NewActivationActivity extends BaseActivity<NewActivationPresenter> implements NewActivationContract.View {

    private final static String TAG = NewActivationActivity.class.getSimpleName();

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

    private List<String> list;
    private ActivationCheckAdapter adapter;

    private Disposable disposableCheckActive;

    @Override
    public int getLayout() {
        return R.layout.avtivity_new_activation;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        tvIccid.setText("iccId:" + MobileInfoUtil.getICCID(this));
        tvImei.setText("imei:" + MobileInfoUtil.getIMEI(this));
        ivActivePadQrcode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID(this)
                + "," + MobileInfoUtil.getIMEI(this), 232, 232));

        list = new ArrayList<>();
        adapter = new ActivationCheckAdapter(R.layout.rv_item_activation_check_msg, list);

        boolean isReboot = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        if (isReboot) {
            // 自启动，只做数据检查
            startCheck();
        } else {
            // 手动启动，需要检查激活
            mPresenter.bindingDevice(MobileInfoUtil.getIMEI(this));
        }

    }

    @Override
    public void initPresenter() {
        mPresenter = new NewActivationPresenter();
    }

    @Override
    public void bindSuccess(ActivePadInfo.DataBean bean) {
        LogUtil.i(TAG, "bindSuccess");
        RxUtil.closeDisposable(disposableCheckActive);
        bean.setHost(UrlUtil.getHost());
        ActiveUtils.setActiveInfo(bean);
        //添加统一header
        HttpHeaders headers = new HttpHeaders();
        headers.put("PAD", bean.getPad());
        OkGo.getInstance().addCommonHeaders(headers);
        JPushInterface.setAlias(this, 2, bean.getBedId() + "pad"
                + bean.getActive());
        tvActiveResult.setText("PAD已激活成功");
        tvTipMaintain.setText("辛苦了运维同事，感谢您的不辞辛劳！");
        startCheck();
    }

    @Override
    public void bindFail() {
        RxUtil.closeDisposable(disposableCheckActive);
        disposableCheckActive = Observable.interval(30, TimeUnit.SECONDS)
                .take(1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mPresenter.bindingDevice(MobileInfoUtil.getIMEI(NewActivationActivity.this));
                    }
                });
    }

    /**
     * 开始检查数据
     */
    private void startCheck() {
//        llCheck.setVisibility(View.VISIBLE);
        list.add("激活已完成，正在初始化影视数据,请稍后...");
        adapter.notifyDataSetChanged();
        VideoService.getInstance().startVideoInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMsg(CheckMsgEvent event) {
        try {
            LogUtil.d(TAG, "msg:" + event.getMsg());
            if (event.isAdd()) {
                list.add(event.getMsg());
            } else {
                list.set(list.size() - 1, event.getMsg());
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
