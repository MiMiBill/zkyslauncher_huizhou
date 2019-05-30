package com.muju.note.launcher.app.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.fragment.PriseFragment;
import com.muju.note.launcher.app.userinfo.fragment.UserInfoFragment;
import com.muju.note.launcher.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_userinfo)
    ImageView ivUserinfo;
    @BindView(R.id.tv_userinfo)
    TextView tvUserinfo;
    @BindView(R.id.rl_userinfo)
    RelativeLayout rlUserinfo;
    @BindView(R.id.iv_prize)
    ImageView ivPrize;
    @BindView(R.id.tv_prize)
    TextView tvPrize;
    @BindView(R.id.rl_prize)
    RelativeLayout rlPrize;
    @BindView(R.id.iv_signin)
    ImageView ivSignin;
    @BindView(R.id.tv_signin)
    TextView tvSignin;
    @BindView(R.id.rl_signin)
    RelativeLayout rlSignin;
    @BindView(R.id.iv_luckdraw)
    ImageView ivLuckdraw;
    @BindView(R.id.tv_luckdraw)
    TextView tvLuckdraw;
    @BindView(R.id.rl_luckdraw)
    RelativeLayout rlLuckdraw;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    private RelativeLayout[] rls;
    private ImageView[] imgs;
    private TextView[] tvs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_userinfo;
    }


    @Override
    public void initData() {
        rls = new RelativeLayout[4];
        rls[0] = rlUserinfo;
        rls[1] = rlPrize;
        rls[2] = rlSignin;
        rls[3] = rlLuckdraw;
        imgs = new ImageView[4];
        imgs[0] = ivUserinfo;
        imgs[1] = ivPrize;
        imgs[2] = ivSignin;
        imgs[3] = ivLuckdraw;
        tvs = new TextView[4];
        tvs[0] = tvUserinfo;
        tvs[1] = tvPrize;
        tvs[2] = tvSignin;
        tvs[3] = tvLuckdraw;

        rls[0].setOnClickListener(this);
        rls[1].setOnClickListener(this);
        rls[2].setOnClickListener(this);
        rls[3].setOnClickListener(this);

        setView(0);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new
                UserInfoFragment()).commit();
    }


    @Override
    public void onClick(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.rl_userinfo:
                index = 0;
                setView(index);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new
                        UserInfoFragment()).commit();
                break;
            case R.id.rl_prize:
                index = 1;
                setView(index);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new
                        PriseFragment()).commit();
                break;
            case R.id.rl_signin:
//                index = 2;
//                setView(index);
                startActivity(new Intent(UserInfoActivity.this, SigninActivity.class));
                break;
            case R.id.rl_luckdraw:
//                index=3;
//                setView(index);
                startActivity(new Intent(UserInfoActivity.this, LuckDrawActivity.class));
                break;
        }
    }

    private void setView(int index) {
        for (int i = 0; i < rls.length; i++) {
            if (i == index) {
                rls[i].setSelected(true);
                tvs[i].setSelected(true);
                imgs[i].setEnabled(true);
            } else {
                rls[i].setSelected(false);
                tvs[i].setSelected(false);
                imgs[i].setEnabled(false);
            }
        }
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
