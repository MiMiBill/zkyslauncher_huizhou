package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.adtask.event.UserInfoEvent;
import com.muju.note.launcher.app.video.bean.UserBean;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.user.UserUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginDialog extends Dialog {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rel_dismiss)
    RelativeLayout ivDissmiss;
    @BindView(R.id.iv_login_code)
    ImageView ivLoginCode;
    @BindView(R.id.llPayCode)
    LinearLayout llPayCode;

    private OnLoginListener loginListener;
    private Context context;

    public LoginDialog(@NonNull Context context, int themeResId, OnLoginListener loginListener) {
        super(context, themeResId);

        this.loginListener=loginListener;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wxlogin);
        EventBus.getDefault().register(LoginDialog.this);
        ButterKnife.bind(this);

        ivDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                loginListener.onFail();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String code=UrlUtil.getWxLogin() + ActiveUtils.getPadActiveInfo().getBedId();//+ MobileInfoUtil.getIMEI(context);
                LogUtil.d("url:%s", code);
                Bitmap bitmap=QrCodeUtils.generateOriginalBitmap(code, 418, 418);
                Message msg=new Message();
                msg.what=0x03;
                msg.obj=bitmap;
                handler.sendMessage(msg);
            }
        }).start();

//        startQueryUser();
        handler.sendEmptyMessageDelayed(0x02,1000*60);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                EventBus.getDefault().unregister(LoginDialog.this);
                OkGo.getInstance().cancelTag(UrlUtil.getUserInfo()+MobileInfoUtil.getIMEI(context));
//                handler.removeMessages(0x01);
                handler.removeMessages(0x02);
                handler=null;
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfoEvent userInfoEvent) {
        LogUtil.d("LoginDialog 收到登录信息");
        loginListener.onSuccess();
    }

    private void startQueryUser(){
        OkGo.<BaseBean<UserBean>>get(UrlUtil.getUserInfo()+MobileInfoUtil.getIMEI(context))
                .tag(UrlUtil.getUserInfo()+MobileInfoUtil.getIMEI(context))
                .execute(new JsonCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
                        if(response.body().getData()==null){
                            handler.sendEmptyMessageDelayed(0x01,1000*2);
                            return;
                        }
                        UserUtil.setUserBean(response.body().getData());
                        EventBus.getDefault().post(new UserInfoEvent(response.body().getData()));
                        loginListener.onSuccess();
                    }

                    @Override
                    public void onError(Response<BaseBean<UserBean>> response) {
                        super.onError(response);

//                        handler.sendEmptyMessageDelayed(0x01,1000*2);
                    }
                });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
//                    startQueryUser();
                    break;

                case 0x02:
                    dismiss();
                    loginListener.onFail();
                    break;

                case 0x03:
                    ivLoginCode.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };


    /**
     *  登录回调
     */
    public interface OnLoginListener{
        public void onSuccess();
        public void onFail();
    }
}
