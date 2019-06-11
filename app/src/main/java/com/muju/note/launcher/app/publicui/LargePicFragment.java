package com.muju.note.launcher.app.publicui;

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
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;

import java.io.File;

import butterknife.BindView;

/**
 * 广告查看大图
 */
public class LargePicFragment extends BaseFragment {
    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;
    @BindView(R.id.iv_dissmiss)
    ImageView ivDissmiss;
    @BindView(R.id.progress)
    ProgressBar progress;

    private static final String LARGE_PIC_TITLE="large_pic_title";
    private static final String LARGE_PIC_ID="large_pic_id";
    private static final String LARGE_PIC_URL="large_pic_url";

    private String title;
    private int advertId;
    private String url;
    private long startTime;
    public static LargePicFragment newInstance(String title, int id,String url) {
        Bundle args = new Bundle();
        args.putString(LARGE_PIC_TITLE, title);
        args.putString(LARGE_PIC_URL, url);
        args.putInt(LARGE_PIC_ID, id);
        LargePicFragment fragment = new LargePicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_large;
    }

    @Override
    public void initData() {

        advertId=getArguments().getInt(LARGE_PIC_ID);
        title=getArguments().getString(LARGE_PIC_TITLE);
        url=getArguments().getString(LARGE_PIC_URL);

        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        startTime = System.currentTimeMillis();
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

        ivDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
    }


    private void doFinish() {
        long currentTime=System.currentTimeMillis();
        NewAdvertsUtil.getInstance().addData(advertId, NewAdvertsUtil.TAG_BROWSETIME,
                currentTime-startTime);
        NewAdvertsUtil.getInstance().addDataInfo(advertId, NewAdvertsUtil.TAG_BROWSETIME,
                startTime, currentTime);
//        EventBus.getDefault().post(new VideoCodeFailEvent(true));
        pop();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }
}
