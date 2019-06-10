package com.muju.note.launcher.app.publicAdress.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.publicAdress.presenter.PublicPresenter;
import com.muju.note.launcher.app.video.event.VideoCodeFailEvent;
import com.muju.note.launcher.app.video.event.VideoReStartEvent;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.password.OnPasswordFinish;
import com.muju.note.launcher.view.password.PopEnterPassword;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicActivity extends BaseActivity<PublicPresenter> implements PublicPresenter
        .PublicListener {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private int adverId = 0;
    private String advertCode = "";
    private long startTime = 0;
    private List<AdvertsBean> adverts = new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.activity_public;
    }

    @Override
    public void initData() {
        mPresenter = new PublicPresenter();
        mPresenter.setOnPublicListener(this);
        adverts = SPUtil.getAdList(AdvertsTopics.CODE_PUBLIC);
        if (adverts != null && adverts.size() > 0) {
            NewAdvertsUtil.getInstance().showByImageView(adverts, ivImg);
            AdvertsBean advertsBean = adverts.get(0);
            tvCode.setText("2:关注后回复验证码" + advertsBean.getCode());
            adverId = advertsBean.getId();
            advertCode = advertsBean.getCode();
            startTime = System.currentTimeMillis();
        }
    }


    @OnClick({R.id.btn_code, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                showPassDialog();
                break;
            case R.id.iv_back:
                EventBus.getDefault().post(new VideoCodeFailEvent(true));
                doFinish();
                break;
        }
    }


    private void doFinish() {
        long currentTime=System.currentTimeMillis();
        NewAdvertsUtil.getInstance().addData(adverId, NewAdvertsUtil.TAG_BROWSETIME, currentTime - startTime);
        NewAdvertsUtil.getInstance().addDataInfo(adverId, NewAdvertsUtil.TAG_SHOWTIME, startTime,currentTime);
        finish();
    }


    //输入验证码框
    private void showPassDialog() {
        PopEnterPassword popEnterPassword = new PopEnterPassword(this);
        // 显示窗口
        popEnterPassword.showAtLocation(this.findViewById(R.id.layoutContent),
                Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
        popEnterPassword.setOnPassFinish(new OnPasswordFinish() {
            @Override
            public void passwordFinish(String password) {
                mPresenter.verfycode(password, adverId, advertCode);
            }
        });
    }


    @Override
    public void verfySuccess(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                showToast("验证码验证成功");
                EventBus.getDefault().post(new VideoReStartEvent(true));
            } else if (jsonObject.optString("msg").contains("已参加")) {
                showToast(jsonObject.optString("msg"));
                EventBus.getDefault().post(new VideoReStartEvent(true));
            } else {
                EventBus.getDefault().post(new VideoCodeFailEvent(true));
            }
            doFinish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verfyFail() {
        LogFactory.l().i("verfyFail");
        EventBus.getDefault().post(new VideoCodeFailEvent(true));
        doFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
