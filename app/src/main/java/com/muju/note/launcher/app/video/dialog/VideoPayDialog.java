package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPayDialog extends Dialog {
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tvHelp)
    TextView tvHelp;
    @BindView(R.id.btPay)
    Button btPay;
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.clPayAlert)
    ConstraintLayout clPayAlert;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivClose2)
    ImageView ivClose2;
    @BindView(R.id.ivPayCode)
    ImageView ivPayCode;
    @BindView(R.id.tv_pay_time1)
    TextView tvPayTime1;
    @BindView(R.id.llPayCode)
    LinearLayout llPayCode;

    private View.OnClickListener listener;

    public VideoPayDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_alert);
        ButterKnife.bind(this);
        ivClose.setOnClickListener(listener);
        ivClose2.setOnClickListener(listener);
        tvHelp.setOnClickListener(listener);
        btPay.setOnClickListener(listener);
        tvLogin.setOnClickListener(listener);
    }

    public void setPay(){
        clPayAlert.setVisibility(View.GONE);
        llPayCode.setVisibility(View.VISIBLE);
    }

    public void setQrde(){
        String code = String.format("https://xiao.zgzkys.com/qrcode?DevName=%s",
                MobileInfoUtil.getIMEI(getContext()));
        ivPayCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(code, 418, 418));
    }
}
