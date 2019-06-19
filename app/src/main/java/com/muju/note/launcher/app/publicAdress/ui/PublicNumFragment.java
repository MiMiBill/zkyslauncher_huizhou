package com.muju.note.launcher.app.publicAdress.ui;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.publicAdress.contract.PublicContract;
import com.muju.note.launcher.app.publicAdress.presenter.PublicPresenter;
import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.password.OnPasswordFinish;
import com.muju.note.launcher.view.password.PopEnterPassword;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class PublicNumFragment extends BaseFragment<PublicPresenter> implements PublicContract.View  {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.layoutContent)
    RelativeLayout layoutContent;
    private int adverId = 0;
    private String advertCode = "";
    private long startTime = 0;
    private static final String PUB_PIC_ID="large_pic_id";
    private static final String PUB_PIC_URL="large_pic_url";
    private int advertId;
    private String url;
    public static PublicNumFragment newInstance(int id, String url) {
        Bundle args = new Bundle();
        args.putString(PUB_PIC_URL, url);
        args.putInt(PUB_PIC_ID, id);
        PublicNumFragment fragment = new PublicNumFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getLayout() {
        return R.layout.activity_public;
    }

    @Override
    public void initData() {
        advertId=getArguments().getInt(PUB_PIC_ID);
        url=getArguments().getString(PUB_PIC_URL);
        Glide.with(LauncherApplication.getContext()).load(url).into(ivImg);
    }

    @Override
    public void initPresenter() {
        mPresenter=new PublicPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.btn_code, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                showPassDialog();
                break;
            case R.id.iv_back:
                doFinish();
                break;
        }
    }


    private void doFinish() {
        long currentTime=System.currentTimeMillis();
        NewAdvertsUtil.getInstance().addData(adverId, NewAdvertsUtil.TAG_BROWSETIME, currentTime - startTime);
        NewAdvertsUtil.getInstance().addDataInfo(adverId, NewAdvertsUtil.TAG_SHOWTIME, startTime,currentTime);
        pop();
    }

    //输入验证码框
    private void showPassDialog() {
        PopEnterPassword popEnterPassword = new PopEnterPassword(getActivity());
        // 显示窗口
        popEnterPassword.showAtLocation(getActivity().findViewById(R.id.layoutContent),
                Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
        popEnterPassword.setOnPassFinish(new OnPasswordFinish() {
            @Override
            public void passwordFinish(String password) {
                mPresenter.verfycode(password, adverId, advertCode);
//                mPresenter.doTask(UserUtil.getUserBean().getId(),advertId);
            }
        });
    }


    @Override
    public void verfycode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                showToast("验证码验证成功");
                mPresenter.doTask(UserUtil.getUserBean().getId(),advertId);
            } else {
                showToast("验证码验证失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doTask(TaskBean taskBean) {
        EventBus.getDefault().post(new UserInfoEvent(UserUtil.getUserBean()));
        doFinish();
    }
}
