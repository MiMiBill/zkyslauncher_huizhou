package com.muju.note.launcher.app.adtask.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.muju.note.launcher.R;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/1.
 */

public class RewardDialog extends Dialog implements View.OnClickListener {

    private String mContent;
    private int mLayoutId;
    private OnRewardListener listener;

    public RewardDialog(@NonNull Context context, String content) {
        super(context, R.style.MyDialogTheme);
        this.mContent = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.custom_msg_dialog_layout);
        setContentView(mLayoutId);
        WindowManager.LayoutParams attr = getWindow().getAttributes();//获取Dialog属性
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        attr.width = (int) (outMetric.widthPixels * 0.52f);
        attr.height = (int) (outMetric.heightPixels * 0.75625f);
        getWindow().setAttributes(attr);
        setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
            getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        } else {
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
//        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        initView();

    }

    private void initView() {
        TextView tvContent = findViewById(R.id.tv_content);
        //展示的内容
        tvContent.setText(mContent);
        //点击
        TextView btnPosition = findViewById(R.id.btn_positive);
        TextView btn_native = findViewById(R.id.btn_native);
        btnPosition.setOnClickListener(this);
        btn_native.setOnClickListener(this);
    }

    public RewardDialog setCustView(int id) {
        this.mLayoutId = id;
        return this;
    }

    @OnClick({R.id.btn_positive, R.id.btn_native})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_positive:
                if (listener != null) {
                    listener.onReward();
                }
                dismiss();
                break;
            case R.id.btn_native:
                dismiss();
                break;
        }
    }

    public interface OnRewardListener {
        void onReward();
    }

    public void setOnRewardListener(OnRewardListener listener) {
        this.listener = listener;
    }
}
