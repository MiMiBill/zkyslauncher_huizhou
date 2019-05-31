package com.muju.note.launcher.app.setting.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.bean.UserBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.user.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserFragment extends BaseFragment {
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.iv_ma)
    ImageView ivMa;
    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.rel_not_login)
    RelativeLayout relNotLogin;
    @BindView(R.id.rel_login)
    RelativeLayout relLogin;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    startQueryUser();
                    break;
                case 0x03:
                    ivMa.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };


    @Override
    public int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void initData() {
        LogFactory.l().i("initData");
    }


    public void setUser() {
        relLogin.setVisibility(View.VISIBLE);
        relNotLogin.setVisibility(View.GONE);
        try {
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getNickname())) {
                tvName.setText(UserUtil.getUserBean().getNickname());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getSex())) {
                tvSex.setText(UserUtil.getUserBean().getSex());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getMobile())) {
                tvPhone.setText(UserUtil.getUserBean().getMobile());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAddress())) {
                tvAddress.setText(UserUtil.getUserBean().getAddress());
            }
            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAvater())) {
                Glide.with(getActivity()).load(UserUtil.getUserBean().getAvater()).apply
                        (RequestOptions.bitmapTransform(new CircleCrop())).into(ivHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startQueryUser() {
        OkGo.<BaseBean<UserBean>>get(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI
                (LauncherApplication.getContext()))
                .tag(UrlUtil.getUserInfo() + MobileInfoUtil.getIMEI(LauncherApplication
                        .getContext()))
                .execute(new JsonCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
                        LogFactory.l().i("response==="+response.body().getData());
                        if (response.body().getData() == null) {
                            handler.sendEmptyMessageDelayed(0x01, 1000 * 3);
                            return;
                        }else {
                            UserUtil.setUserBean(response.body().getData());
                            setUser();
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<UserBean>> response) {
                        super.onError(response);
                        handler.sendEmptyMessageDelayed(0x01, 1000 * 3);
                    }
                });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = inflater.inflate(R.layout.fragment_user,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        showView();
        return rootView;
    }

    private void showView() {
        if (UserUtil.getUserBean() != null) {
            //登录成功
            LogFactory.l().i("登录成功");
            setUser();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String code = UrlUtil.getWxLogin() + MobileInfoUtil.getIMEI
                            (LauncherApplication.getContext());
                    LogFactory.l().i("code=="+code);
                    Bitmap bitmap = QrCodeUtils.generateOriginalBitmap(code, 468, 468);
                    Message msg = new Message();
                    msg.what = 0x03;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }).start();
            relLogin.setVisibility(View.GONE);
            relNotLogin.setVisibility(View.VISIBLE);
            startQueryUser();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeMessages(0x01);
        handler.removeMessages(0x03);
        unbinder.unbind();
    }

}
