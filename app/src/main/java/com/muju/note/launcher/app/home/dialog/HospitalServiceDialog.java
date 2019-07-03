package com.muju.note.launcher.app.home.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HospitalServiceDialog extends Dialog {

    @BindView(R.id.textView13)
    TextView textView13;
    @BindView(R.id.view9)
    View view9;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.view10)
    View view10;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tvHint)
    TextView tvHint;
    private int type = 0;
    private OnClickListener listener;

    public HospitalServiceDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_service_login_layout);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnConfirm, R.id.ivClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if(listener!=null){
                    listener.onClick(etNumber.getText().toString().trim());
                }
                break;
            case R.id.ivClose:
                dismiss();
                break;
        }
    }


    public interface OnClickListener{
         void onClick(String pass);
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
}