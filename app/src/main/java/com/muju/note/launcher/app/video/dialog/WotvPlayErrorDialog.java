package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.muju.note.launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WotvPlayErrorDialog extends Dialog {
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.btn_dissmiss)
    Button btnDissmiss;

    private View.OnClickListener listener;

    public WotvPlayErrorDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wotv_play_error);
        ButterKnife.bind(this);

        ivDissmiss.setOnClickListener(listener);
        btnDissmiss.setOnClickListener(listener);
    }
}
