package com.muju.note.launcher.app.Cabinet.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.bean.CabinetBean;
import com.muju.note.launcher.app.Cabinet.contract.CabinetContract;
import com.muju.note.launcher.app.Cabinet.event.ReturnBedEvent;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetPresenter;
import com.muju.note.launcher.app.luckdraw.ui.LuckDrawFragment;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.view.MyVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 屏安柜
 */
public class CabinetFragment extends BaseFragment<CabinetPresenter> implements CabinetContract
        .View {
    private static final String TAG = "CabinetFragment";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_use)
    TextView tvUse;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.lly_not_login)
    LinearLayout llyNotLogin;
    @BindView(R.id.lly_nocabnet)
    LinearLayout llyNoCabinet;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.lly_login)
    LinearLayout llyLogin;
    @BindView(R.id.lly_prise)
    LinearLayout llyPrise;
    @BindView(R.id.lly_orser)
    LinearLayout llyOrser;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_rent_time)
    TextView tvRentTime;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;
    @BindView(R.id.btn_lock)
    Button btnLock;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.video_view)
    MyVideoView videoView;
    private boolean isOrder = false;
    private CabinetBean.DataBean dataBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeMessages(1);
                    if (isOrder) {
                        setTime();
                    }
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragment_cabinet;
    }

    @Override
    public void initData() {
        tvTitle.setText("屏安柜");
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        EventBus.getDefault().register(this);
        mPresenter.getCabnetOrder();
        playVideo();
    }

    @Override
    public void initPresenter() {
        mPresenter = new CabinetPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        handler.removeMessages(1);
        if(videoView!=null){
            videoView.stopPlayback();
        }
        EventBus.getDefault().post(new VideoNoLockEvent(true));
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }


    @OnClick({R.id.lly_prise, R.id.lly_orser, R.id.btn_unlock, R.id.btn_lock,R.id.iv_cabinet_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_prise:
                start(new LuckDrawFragment());
                break;
            case R.id.lly_orser:
                if (isOrder) {
                    start(CabinetOrderFragment.newInstance(dataBean));
                }
                break;
            case R.id.btn_unlock:
                if (isOrder) {
                    start(UnlockFragment.newInstance(dataBean));
                }
                break;
            case R.id.btn_lock:
                if (isOrder) {
                    start(ReturnBedFragment.newInstance(dataBean));
                }
                break;
            case R.id.iv_cabinet_play:
                playVideo();
                break;
        }
    }

    //播放视频
    private void playVideo() {
        PadConfigSubDao subDao = LitePal.where("type =?", "openVideo").findFirst(PadConfigSubDao
        .class);
        if(subDao!=null){
            String path=subDao.getContent();
            String videoPath=SdcardConfig.RESOURCE_FOLDER+path.hashCode()+".mp4";
            Uri uri=Uri.parse(videoPath);
            videoView.setVideoURI(uri);
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
            EventBus.getDefault().post(new VideoNoLockEvent(false));
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.stopPlayback();
                    videoView.setVisibility(View.GONE);
                }
            });
        }
    }


    //推送成功或者订单页面归还成功,重新拉取订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReturnBedEvent event) {
        mPresenter.getCabnetOrder();
    }


    @Override
    public void getOrder(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                Gson gson = new Gson();
                CabinetBean cabinetBean = gson.fromJson(data, CabinetBean.class);
                dataBean = cabinetBean.getData();
                if(dataBean.getHasLock()==1){  //有柜子有订单
                    if(dataBean.getId()>0){
                        isOrder = true;
                        llyNotLogin.setVisibility(View.GONE);
                        llyLogin.setVisibility(View.VISIBLE);
                        btnLock.setEnabled(true);
                        btnUnlock.setEnabled(true);
                        btnUnlock.setBackgroundColor(getResources().getColor(R.color.color_38B4E9));
                        btnLock.setBackgroundColor(getResources().getColor(R.color.color_FF6339));
                        tvEnd.setText(dataBean.getExpireTime());
                        tvStart.setText(dataBean.getLeaseTime());
                        tvRentTime.setText(dataBean.getNum() + "天");
                        setTime();
                    }else { //有柜子无订单
                        noOrder(0);
                    }
                }else { //无柜子
                    noOrder(1);
                }
            } else { //无柜子
                noOrder(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //设置归还倒计时
    private void setTime() {
        handler.sendEmptyMessageDelayed(1, 1000 * 60);
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        long lastTime = DateUtil.formartTime(dataBean.getExpireTime());
        tvTime.setText(DateUtil.getTime((int) (lastTime - currentTimeMillis)));
    }

    //无订单
    private void noOrder(int type) {
        isOrder = false;
        llyLogin.setVisibility(View.GONE);
        if(type==0){
            llyNoCabinet.setVisibility(View.GONE);
            llyNotLogin.setVisibility(View.VISIBLE);
            String code = String.format("https://xiao.zgzkys.com/qrcode?DevName=%s", MobileInfoUtil.getIMEI(getContext()));
            ivCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(code, 102, 102));
        }else {
            llyNotLogin.setVisibility(View.GONE);
            llyNoCabinet.setVisibility(View.VISIBLE);
        }
        tvStart.setText("暂无信息");
        tvEnd.setText("暂无信息");
        tvRentTime.setText("暂无信息");
        btnLock.setEnabled(false);
        btnUnlock.setEnabled(false);
        btnUnlock.setBackgroundColor(getResources().getColor(R.color.black_gray1));
        btnLock.setBackgroundColor(getResources().getColor(R.color.black_gray1));
    }

    @Override
    public void unLock(String data) {

    }

    @Override
    public void padNoOrder() {
        noOrder(1);
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
    public void findByDid(String data) {

    }

}
