package com.muju.note.launcher.app.home.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muju.note.launcher.R;
import com.muju.note.launcher.util.toast.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HospitalServiceDialog extends Dialog {

    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.rl_zxing)
    RelativeLayout rlZxing;

    private View.OnClickListener listener;
    private Context context;

    public HospitalServiceDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
        this.listener = listener;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hospital_service);
        ButterKnife.bind(this);

        ivDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnLogin.setOnClickListener(listener);

        rlZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyToast.makeText(context,"功能暂未开通，敬请期待...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getEtNum() {
        return etNum.getText().toString();
    }
}