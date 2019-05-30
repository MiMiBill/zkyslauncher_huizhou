package com.muju.note.launcher.app.setting.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.UserInfoActivity;
import com.muju.note.launcher.app.video.bean.UserBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.user.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserFragment extends BaseFragment {
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.iv_ma)
    ImageView ivMa;
    Unbinder unbinder;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    startQueryUser();
                    break;
                case 0x02:
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> observableEmitter) throws Exception {

                Bitmap bitmap = QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID(LauncherApplication.getContext())
                        + "," + MobileInfoUtil.getIMEI(getContext()), 468, 468);
                observableEmitter.onNext(bitmap);


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        ivMa.setImageBitmap(bitmap);
                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String code=UrlUtil.getWxLogin()+ MobileInfoUtil.getIMEI(LauncherApplication.getContext());
                LogUtil.d("code:%s", code);
                Bitmap bitmap=QrCodeUtils.generateOriginalBitmap(code, 468, 468);
                Message msg=new Message();
                msg.what=0x03;
                msg.obj=bitmap;
                handler.sendMessage(msg);
            }
        }).start();

        startQueryUser();
        handler.sendEmptyMessageDelayed(0x02,1000*60);
    }

    private void startQueryUser(){
        OkGo.<BaseBean<UserBean>>get(UrlUtil.getUserInfo()+MobileInfoUtil.getIMEI(LauncherApplication.getContext()))
                .tag(UrlUtil.getUserInfo()+MobileInfoUtil.getIMEI(LauncherApplication.getContext()))
                .execute(new JsonCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
                        if(response.body().getData()==null){
                            handler.sendEmptyMessageDelayed(0x01,1000*3);
                            return;
                        }
                        UserUtil.setUserBean(response.body().getData());
                        //登录成功
                        startActivity(new Intent(getActivity(),UserInfoActivity.class));
                    }

                    @Override
                    public void onError(Response<BaseBean<UserBean>> response) {
                        super.onError(response);
                        handler.sendEmptyMessageDelayed(0x01,1000*3);
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
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
