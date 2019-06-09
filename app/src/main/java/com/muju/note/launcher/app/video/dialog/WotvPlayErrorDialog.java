package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WotvPlayErrorDialog extends Dialog {
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.btn_dissmiss)
    Button btnDissmiss;
    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private View.OnClickListener listener;
    private String msg;

    public WotvPlayErrorDialog(Context context, int themeResId,String msg, View.OnClickListener listener) {
        super(context, themeResId);
        this.listener = listener;
        this.msg=msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wotv_play_error);
        ButterKnife.bind(this);

        if(!TextUtils.isEmpty(msg)) {
            tvMsg.setText("错误信息:"+msg);
        }

        ivDissmiss.setOnClickListener(listener);
        btnDissmiss.setOnClickListener(listener);
    }
}
