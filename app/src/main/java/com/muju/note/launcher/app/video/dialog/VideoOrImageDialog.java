package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.muju.note.launcher.R;
import com.muju.note.launcher.base.LauncherApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoOrImageDialog extends Dialog {
    @BindView(R.id.iv_advert)
    ImageView ivAdvert;
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.iv_video)
    VideoView ivVideo;
    @BindView(R.id.rel_ad)
    RelativeLayout relAd;
    private View.OnClickListener listener;
    private OnAdDialogDismissListener dismissListener;
    private Context context;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    dismissListener.OnAdDialogDismiss();
                    break;
            }
        }
    };
    public VideoOrImageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void closeBySelf(int second){
        handler.sendEmptyMessageDelayed(1,second*1000);
    }

    public void removeHandler(){
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video);
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
        ivVideo.setVisibility(View.GONE);

        Glide.with(LauncherApplication.getContext()).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ivAdvert.setImageDrawable(resource);
                relAd.setVisibility(View.VISIBLE);
            }
        });
        if(closeType==2){
            ivDissmiss.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(1,second*1000);
        }
    }

    public void startVideo(String url) {
        ivAdvert.setVisibility(View.GONE);
        ivVideo.setVideoPath(url);
        ivVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                relAd.setVisibility(View.VISIBLE);
                mp.setVolume(0f, 0f);
                ivVideo.start();
            }
        });
//        ivVideo.setZOrderOnTop(true);
    }

    public void setOnAdDialogDismissListener(OnAdDialogDismissListener listener){
        this.dismissListener=listener;
    }

}
