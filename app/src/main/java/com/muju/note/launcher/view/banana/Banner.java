package com.muju.note.launcher.view.banana;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.muju.note.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2018/5/14.
 */
//public class Banner extends RelativeLayout
public class Banner extends LinearLayout {
    private ViewPagerBanner viewPager;
    private final int UPTATE_VIEWPAGER = 100;
    //图片默认时间间隔
    private int imgDelyed = 10000;
    //每个位置默认时间间隔，因为有视频的原因
    private int delyedTime = 10000;
    //默认显示位置
    private int autoCurrIndex = 0;
    private int currentIndex = 0;
    //是否自动播放
    private boolean isAutoPlay = false;
    private Time time;
    private List<BannerModel> bannerModels = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private BannerViewAdapter mAdapter;
    private OnBannerListener listener;
    private OnBannerImageSuccessListener imageSuccessListener;
    private List<BannerPage> listBanner = new ArrayList<>();
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public Banner(Context context) {
        super(context);
        init(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * //@RequiresApi
     **/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        BannerModel.newProxy(context.getApplicationContext());

        time = new Time();
        LayoutParams vp_param = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
//        vp_param.gravity = Gravity.CENTER;
//        vp_param.gravity = Gravity.BOTTOM | Gravity.START;

//        setLayoutParams(vp_param);

        viewPager = new ViewPagerBanner(getContext());
        viewPager.setLayoutParams(vp_param);

        //viewPager.setBackgroundColor(Color.RED);
        this.addView(viewPager);
    }

    /**
     * 设置图片延迟时间
     *
     * @param imgDelyed int;//时间（ms:单位毫秒）
     **/
    public void setImgDelyed(int imgDelyed) {
        this.imgDelyed = imgDelyed;
    }

    /**
     * 设置ViewPager是否可以滑动，默认false不可滑动
     *
     * @param isSlide boolean;//
     **/
    public void setSlide(boolean isSlide) {
        viewPager.setSlide(isSlide);
    }

    /**
     * 获取delyedTime
     *
     * @param position 当前位置
     */
    private void getDelayedTime(int position) {
        View view1 = views.get(position);
        if (view1 instanceof VideoView) {
            VideoView videoView = (VideoView) view1;
            videoView.start();
            videoView.seekTo(0);
            //videoView.suspend();//挂起视频文件的播放
            //videoView.resume ();//恢复挂起的播放器
            delyedTime = videoView.getDuration();//单位毫秒（ms）
            //delyedTime = videoView.getDuration() - videoView.getCurrentPosition();//单位毫秒（ms）
            time.getDelyedTime(videoView, runnable);
        } else {
            //delyedTime = imgDelyed;
            delyedTime = listBanner.get(position).imgDelyed;
            ;
        }
    }

    public void setCurrentItem(int position) {
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置滚动广告，图片，视频
     **/
    public void startBanner() {
        mAdapter = new BannerViewAdapter(views, getContext());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);//参数1的含义就是，除去当前显示页面以外需要被预加载的页面数
        viewPager.setCurrentItem(autoCurrIndex, false);//跳转到指定页面
        viewPager.addOnPageChangeListener(new ViewPagerBanner.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset,
                            positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                LogFactory.l().i("position:"+position);
                //当前位置
                autoCurrIndex = position;
                getDelayedTime(position);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d("zkpad",""+state);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }

                //移除自动计时
//                if(mHandler!=null){
                mHandler.removeCallbacks(runnable);
//                }

                //ViewPager跳转
                int pageIndex = autoCurrIndex;
                if (autoCurrIndex == 0) {
                    pageIndex = views.size() - 2;
                } else if (views != null && autoCurrIndex == views.size() - 1) {
                    pageIndex = 1;
                }
                if (pageIndex != autoCurrIndex) {
                    //无滑动动画，直接跳转
                    viewPager.setCurrentItem(pageIndex, false);
                }

                //停止滑动时，重新自动倒计时
                if (state == 0 && isAutoPlay && views.size() > 1) {
                    View view1 = views.get(pageIndex);
                    /*if(view1 instanceof RelativeLayout){ //画中画

                    }else {*/
                        if (view1 instanceof VideoView) {
                            final VideoView videoView = (VideoView) view1;
                            int current = videoView.getCurrentPosition();//获取到当前播放的时间位置
                            int duration = videoView.getDuration();
                            delyedTime = duration - current;

                            //某些时候，某些视频，获取的时间无效，就延时10秒，重新获取
                            if (delyedTime <= 0) {
                                time.getDelyedTime(videoView, runnable);
                                mHandler.postDelayed(time, imgDelyed);
                            } else {
                                mHandler.postDelayed(runnable, delyedTime);
                            }
                        } else {
                            //delyedTime = imgDelyed;
                            delyedTime = listBanner.get(pageIndex).imgDelyed;
                            mHandler.postDelayed(runnable, delyedTime);
                        }
                    }
                    currentIndex = pageIndex;
                }

//            }
        });
    }


    public int getCurrentIndex() {
        return currentIndex;
    }


    //开启自动循环
    public void startAutoPlay() {
        isAutoPlay = true;
        if (views.size() > 1) {
            getDelayedTime(autoCurrIndex);
            if (delyedTime <= 0) {
                mHandler.postDelayed(time, imgDelyed);
            } else {
                mHandler.postDelayed(runnable, delyedTime);
            }
        }
    }

    //关闭自动循环
    public void stopAutoPlay() {
        isAutoPlay = false;
        mHandler.removeCallbacks(null);
    }

    /**
     * 发消息，进行循环
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(UPTATE_VIEWPAGER);
        }
    };

    /**
     * 这个类，恩，获取视频长度，以及已经播放的时间
     */
    private class Time implements Runnable {

        private VideoView videoView;
        private Runnable runnable;

        public void getDelyedTime(VideoView videoView, Runnable runnable) {
            this.videoView = videoView;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            int current = videoView.getCurrentPosition();
            int duration = videoView.getDuration();
            int delyedTime = duration - current;
            mHandler.postDelayed(runnable, delyedTime);
        }
    }

    //接受消息实现轮播
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_VIEWPAGER:
                    viewPager.setCurrentItem(autoCurrIndex + 1, true);
                    break;
            }
        }
    };

   /* private class BannerModel{
        public String url;
        public int playTime;
        public int type = 0;
    }*/

    /**
     * @param dataList,//轮播数组
     **/
    //public void setDataList(List<String> dataList){
    public void setDataList(List<BannerPage> dataList) {
        setDataList(dataList, -1);
    }

    /**
     * 设置数据
     *
     * @param dataList,//轮播数组
     * @param index,//插播地址，   -1时采用默认
     **/
    //public void setDataList(List<String> dataList,int index){
    public void setDataList(List<BannerPage> dataList, int index) {
        listBanner = new ArrayList<>();

        if (index != -1) {
            autoCurrIndex = index;
        }

        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        //用于显示的数组
        if (views == null) {
            views = new ArrayList<>();
        } else {
            views.clear();
        }

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,//ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        //数据大于一条，才可以循环
        if (dataList.size() > 1) {
            autoCurrIndex = 1;
            //循环数组，将首尾各加一条数据
            for (int i = 0; i < dataList.size() + 2; i++)
            //for (int i = 0; i < dataList.size(); i++)
            {

                //String url;
                BannerPage bannerPage;
                if (i == 0) {
                    //url = dataList.get(dataList.size() - 1);
                    bannerPage = dataList.get(dataList.size() - 1);
                } else if (i == dataList.size() + 1) {
                    // url = dataList.get(0);
                    bannerPage = dataList.get(0);
                } else {
                    //url = dataList.get(i - 1);
                    bannerPage = dataList.get(i - 1);
                }

                listBanner.add(bannerPage);

                    if (bannerPage.url.endsWith("mp4")) {
                        MVideoView videoView = new MVideoView(getContext());
                        videoView.setLayoutParams(lp);
                        String s = BannerModel.getProxyUrl(bannerPage.url);
                        videoView.setVideoURI(Uri.parse(BannerModel.getProxyUrl(bannerPage.url)));
                        //videoView.start();
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setVolume(0f, 0f);
                            }
                        });
                        views.add(videoView);
                    } else {
                        final ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(lp);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    LogFactory.l().e("index==="+index);
                        if (index == -100) {
                            if (bannerPage.url.equals("00")) {
                                Glide.with(getContext()).load(R.mipmap.lock_live).apply
                                        (options).into(imageView);
                            } else if (bannerPage.url.equals("11")) {
                                Glide.with(getContext()).load(R.mipmap.lock_video).apply(options)
                                        .into(imageView);
                            } else if (bannerPage.url.equals("22")) {
                                Glide.with(getContext()).load(R.mipmap.banana_live).apply
                                        (options).into(imageView);
                            } else if (bannerPage.url.equals("33")) {
                                Glide.with(getContext()).load(R.mipmap.banana_default).apply
                                        (options).into(imageView);
                            }
                            else if (bannerPage.url.equals("44")) {
                                Glide.with(getContext()).load(R.mipmap.banana_video).apply
                                        (options).into(imageView);
                            }
                            else if (bannerPage.url.equals("55")) {
                                Glide.with(getContext()).load(R.mipmap.lock_movie).apply
                                        (options).into(imageView);
                            }
                        } else {
//                        Glide.with(getContext()).load(bannerPage.url).apply(options).into
// (imageView)
                            Glide.with(getContext()).load(bannerPage.url).apply(options).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable
                                        Transition<? super Drawable> transition) {
                                    if(imageView!=null){
                                        imageView.setImageDrawable(resource);
                                        if (imageSuccessListener != null) {
                                            imageSuccessListener.OnImageSuccess();
                                        }
                                    }
                                }
                            });
                        }
                        final int finalI = i;
                        if (i == 0) {
                            imageView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (listener != null) {
                                        listener.OnBannerClick(finalI);
                                    }
                                }
                            });
                        } else if (i == dataList.size() + 1) {
                            imageView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (listener != null) {
                                        listener.OnBannerClick(finalI);
                                    }
                                }
                            });
                        } else {
                            imageView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (listener != null) {
                                        listener.OnBannerClick(finalI - 1);
                                    }
                                }
                            });
                        }

                        views.add(imageView);
                    }
//                }
            }
        } else if (dataList.size() == 1) {
            autoCurrIndex = 0;
            //String url = dataList.get(0);
            BannerPage bannerPage = dataList.get(0);
            listBanner.add(bannerPage);

          /*  if (bannerPage.videourl.endsWith("mp4")) {    //画中画
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                relativeLayout.setLayoutParams(lp);
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp);
                Glide.with(getContext()).load(bannerPage.url).apply(options).into(imageView);
                MVideoView videoView = new MVideoView(getContext());
                videoView.setLayoutParams(new LayoutParams(356, 270));
                relativeLayout.addView(imageView);
                relativeLayout.addView(videoView);
                videoView.setVideoURI(Uri.parse(BannerModel.getProxyUrl(bannerPage.videourl)));
                //videoView.start();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(0f, 0f);
                        mp.start();
                    }
                });
                views.add(relativeLayout);
            } else {*/
                if (MimeTypeMap.getFileExtensionFromUrl(bannerPage.url).equals("mp4")) {
                    MVideoView videoView = new MVideoView(getContext());
                    videoView.setLayoutParams(lp);
                    videoView.setVideoURI(Uri.parse(BannerModel.getProxyUrl(bannerPage.url)));
//                videoView.start();
                    //监听视频播放完的代码
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mPlayer) {
                            mPlayer.start();
                            mPlayer.setLooping(true);
                        }
                    });
                    views.add(videoView);
                } else {
                    final ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                Glide.with(getContext()).load(bannerPage.url).apply(options).into(imageView);
                    Glide.with(getContext()).load(bannerPage.url).apply(options).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable
                                Transition<? super Drawable> transition) {
                            if(imageView!=null){
                                imageView.setImageDrawable(resource);
                                if (imageSuccessListener != null) {
                                    imageSuccessListener.OnImageSuccess();
                                }
                            }
                        }
                    });
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.OnBannerClick(0);
                            }
                        }
                    });
                    views.add(imageView);
                }
//            }

        } else {
            views.add(new LinearLayout(getContext()));
        }

        //List<BannerPage> l = listBanner;
    }


    /**
     * 设置数据并播放
     *
     * @param dataList,//轮播数组
     **/
    public void setDataPlay(List<BannerPage> dataList) {
        setDataPlay(dataList, -1);
    }

    /**
     * 设置数据并播放
     *
     * @param dataList,//轮播数组
     * @param index,//插播地址，   -1时采用默认
     **/
    public void setDataPlay(List<BannerPage> dataList, int index) {
        setDataList(dataList, index);
        startBanner();
        startAutoPlay();
    }

    /**
     * @param list,//轮播数组
     * @param index,//插播地址， -1时采用默认
     **/
    //public void dataChange(List<String> list,int index){
    public void dataChange(List<BannerPage> list, int index) {
        if (list != null && list.size() > 0) {
            if (index != -1) {
                autoCurrIndex = index;
            }
            //改变资源时要重新开启循环，否则会把视频的时长赋给图片，或者相反
            //因为delyedTime也要改变，所以要重新获取delyedTime
            mHandler.removeCallbacks(runnable);
            setDataList(list);
            mAdapter.setDataList(views, getContext());
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(autoCurrIndex, false);
            //开启循环
            if (isAutoPlay && views.size() > 1) {
                getDelayedTime(autoCurrIndex);
                if (delyedTime <= 0) {
                    mHandler.postDelayed(time, imgDelyed);
                } else {
                    mHandler.postDelayed(runnable, delyedTime);
                }
            }
        }
    }

    public void destroy() {
        //说当参数为null时,删除所有回调函数和message
        // 这样做的好处是在Acticity退出的时候，可以避免内存泄露
//        Log.e("zkpad","banana===destroy");
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        time = null;
        runnable = null;
        views.clear();
        views = null;
        viewPager = null;
        mAdapter = null;
    }

    public Banner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }


    public Banner setOnBannerImageListener(OnBannerImageSuccessListener listener) {
        this.imageSuccessListener = listener;
        return this;
    }
}
