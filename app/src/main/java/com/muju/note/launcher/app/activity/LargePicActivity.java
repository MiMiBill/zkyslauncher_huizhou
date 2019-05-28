package com.muju.note.launcher.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.muju.note.launcher.R;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.log.LogFactory;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//查看大图
public class LargePicActivity extends Activity {
    public static final String PIC_URL = "large_picUrl";
    public static final String PIC_ID = "large_picId";
    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.progress)
    ProgressBar progress;
    private String url = "";
    private int advertId = 0;
    private long startTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_pic);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra(PIC_URL) != null) {
            url = getIntent().getStringExtra(PIC_URL);
            showImg(url);
        }
        startTime = System.currentTimeMillis();

    }


    public static void launch(Context context, String url, int advertId) {
        Intent intent = new Intent(context, LargePicActivity.class);
        intent.putExtra(PIC_URL, url);
        intent.putExtra(PIC_ID, advertId);
        context.startActivity(intent);
    }

    private void showImg(String url) {
        LogFactory.l().i(url);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);

        imageView.setMinScale(1.0F);//最小显示比例
        imageView.setZoomEnabled(false);
        imageView.setMaxScale(1.0F);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）


        Glide.with(this).load(url).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File
                    > transition) {
                imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState
                        (1.0F, new PointF(0, 0), 0));
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void doFinish() {
        NewAdvertsUtil.getInstance().addDataInfo(advertId, NewAdvertsUtil.TAG_BROWSETIME,
                startTime, System.currentTimeMillis());
//        EventBus.getDefault().post(new VideoCodeFailEvent(true));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick(R.id.iv_dissmiss)
    public void onViewClicked() {
        doFinish();
    }
}
