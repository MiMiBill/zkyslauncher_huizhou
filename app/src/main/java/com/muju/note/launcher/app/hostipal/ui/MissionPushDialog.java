package com.muju.note.launcher.app.hostipal.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.util.gilde.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MissionPushDialog extends Dialog {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.btn_toMission)
    Button btnToMission;

    private String url,name;
    private View.OnClickListener listener;

    public MissionPushDialog(Context context, int themeResId,String url,String name,View.OnClickListener listener) {
        super(context, themeResId);
        this.name=name;
        this.url=url;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mission_push);
        ButterKnife.bind(this);

        tvName.setText(name);
        GlideUtil.loadImg(url,ivImg,R.mipmap.ic_video_load_default);

        btnToMission.setOnClickListener(listener);

    }
}
