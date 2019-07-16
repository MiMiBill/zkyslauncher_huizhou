package com.muju.note.launcher.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.dialog.OnAdDialogDismissListener;
import com.muju.note.launcher.base.LauncherApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertsDialog extends Dialog {
    @BindView(R.id.iv_advert)
    ImageView ivAdvert;
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.rel_dialog)
    RelativeLayout relDialog;

    private View.OnClickListener listener;
    private OnAdDialogDismissListener dismissListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dismissListener.OnAdDialogDismiss();
                    break;
            }
        }
    };

    public AdvertsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void removeHandler() {
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_advert);
        ButterKnife.bind(this);

        ivDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ivAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public void setOnImgClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void loadImg(String url, int closeType, int second) {
//        Glide.with(LauncherApplication.getContext()).load(url).into(ivAdvert);
        if (closeType == 2) {
            ivDissmiss.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(1, second * 1000);
        }
        Glide.with(LauncherApplication.getContext()).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super
                    Drawable> transition) {
                ivAdvert.setImageDrawable(resource);
                relDialog.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setOnAdDialogDismissListener(OnAdDialogDismissListener listener) {
        this.dismissListener = listener;
    }

}
