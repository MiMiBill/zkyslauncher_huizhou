package com.muju.note.launcher.view;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.system.SystemUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by FuKaiqiang on 2018-01-06.
 */

public class VideoPlayControlView extends FrameLayout {

    private PlayVideoView videoView;
    private TextView tvTimeCurrent;
    private TextView tvTimeTotal;
    private ImageView ivPause;

    private ImageView ivBack;


    private LinearLayout llStatus;
    private TextView tvStatus;

    private LinearLayout llOperation;
    private ImageView ivBack2;
    private ImageView ivReplay;
    private RelativeLayout layoutLoading;

    private static final int NONE = 0, VOLUME = 1, BRIGHTNESS = 2, FF_REW = 3;

    @ScrollMode
    private int mScrollMode = NONE;


//    private WindowManager.LayoutParams mLayoutParams;


    private AudioManager mAudioManager;

    //当前亮度
//    private float brightness;

    private float oldVolume;
    private int maxVolume;

    private int newProgress;
//    private float oldProgress;

    private BrightnessChangeListener brightnessChangeListener;
    private BackListener backListener;

    private Disposable diUpdate;
    private Disposable diAutoHide;

    public void setBrightnessChangeListener(BrightnessChangeListener brightnessChangeListener) {
        this.brightnessChangeListener = brightnessChangeListener;
    }

    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }

    public VideoView getVideoView() {
        return videoView;
    }


    @IntDef({NONE, VOLUME, BRIGHTNESS, FF_REW})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ScrollMode {

    }
    private VideoPlayerOnGestureListener mOnGestureListener;

    private GestureDetector mGestureDetector;
    private VideoGestureListener mVideoGestureListener;
    //横向偏移检测，让快进快退不那么敏感
    private int offsetX = 1;
    private boolean hasFF_REW = false;


    public VideoPlayControlView(Context context) {
        this(context, null);
    }

    public VideoPlayControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        inflate(context, R.layout.layout_my_videoview, this);
        videoView = findViewById(R.id.vvVideo);
        tvTimeCurrent = findViewById(R.id.tvTimeCurrent);
        tvTimeTotal = findViewById(R.id.tvTimeTotal);
        ivPause = findViewById(R.id.ivPause);
        llStatus = findViewById(R.id.llStatus);
        tvStatus = findViewById(R.id.tvStatus);
        ivBack = findViewById(R.id.ivBack);

        llOperation = findViewById(R.id.llOperation);
        ivBack2 = findViewById(R.id.ivBack2);
        ivReplay = findViewById(R.id.ivReplay);
        layoutLoading = findViewById(R.id.layoutLoading);

        //播放监听
        videoView.setPlayListener(new PlayVideoView.PlayListener() {
            @Override
            public void start() {
                layoutLoading.setVisibility(GONE);
                ivPause.setVisibility(GONE);
            }

            @Override
            public void pause() {
                ivPause.setVisibility(VISIBLE);
            }
        });
        //暂停播放
        ivPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });
        ivBack2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llOperation.setVisibility(GONE);
                backListener.back();
            }
        });

        ivReplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llOperation.setVisibility(GONE);
                getVideoView().start();
            }
        });

        //返回
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backListener.back();
            }
        });


        getVideoView().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.d("################### 播放完成");
                llOperation.setVisibility(VISIBLE);
            }
        });
        getVideoView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtil.d("################### 播放失败  what: %s  extra:%s", what, extra);
                llOperation.setVisibility(VISIBLE);
                return false;
            }
        });
    }

    public void onDestroy() {
        RxUtil.closeDisposable(diUpdate);
        RxUtil.closeDisposable(diAutoHide);

        videoView.suspend();
    }



    private void init(Context context) {
        mOnGestureListener = new VideoPlayerOnGestureListener(this);
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
        //取消长按，不然会影响滑动
        mGestureDetector.setIsLongpressEnabled(false);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                LogUtil.d( "onTouch: event:" + event.toString());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (hasFF_REW) {
                        if (mVideoGestureListener != null) {
                            mVideoGestureListener.onEndFF_REW(event);
                        }
                        hasFF_REW = false;
                    }
                }
                //监听触摸事件
                return mGestureDetector.onTouchEvent(event);
            }
        });


        //初始化获取音量属性
        mAudioManager = (AudioManager)getContext().getSystemService(Service.AUDIO_SERVICE);
//        if(SPUtil.getLong())

       /* if(SPUtil.getLong(SpTopics.PAD_CONFIG_VOLUME_RATE)>=0){
            maxVolume=(int)SPUtil.getLong(SpTopics.PAD_CONFIG_VOLUME_RATE);
        }else {*/
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        }

        setVideoGestureListener(new VideoGestureListener() {
            @Override
            public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                //下面这是设置当前APP亮度的方法
                if (brightnessChangeListener != null) {
                    LogUtil.d("onBrightnessGesture: old" + brightnessChangeListener.getBrightness());
                    float newBrightness = (e1.getY() - e2.getY()) / getHeight();
                    newBrightness += brightnessChangeListener.getBrightness();

                    LogUtil.d("onBrightnessGesture: new" + newBrightness);
                    if (newBrightness < 0) {
                        newBrightness = 0;
                    } else if (newBrightness > 1) {
                        newBrightness = 1;
                    }
                    showStatus(String.format("亮度%s%%", (int) (newBrightness * 100)));
                    brightnessChangeListener.changeBrightness(newBrightness);
                }
            }

            @Override
            public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                LogUtil.d( "onVolumeGesture: oldVolume " + oldVolume);
                int value = getHeight()/maxVolume ;
                int newVolume = (int) ((e1.getY() - e2.getY())/value + oldVolume);
                int volumeProgress = (int) (newVolume / Float.valueOf(maxVolume) * 100);
                volumeProgress = volumeProgress > 0 ? volumeProgress : 0;
                volumeProgress = volumeProgress > 100 ? 100 : volumeProgress;
                SystemUtils.setVolume(getContext(), volumeProgress);


//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,newVolume,AudioManager.FLAG_PLAY_SOUND);
                LogUtil.d( "onVolumeGesture: value" + value);
                LogUtil.d( "onVolumeGesture: newVolume "+ newVolume);
                LogUtil.d( "onVolumeGesture: volumeProgress "+ volumeProgress);
                showStatus(String.format("音量 %s%% ", volumeProgress));
            }

            @Override
            public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float offset = e2.getX() - e1.getX();
                LogUtil.d( "onFF_REWGesture: offset " + offset);
                LogUtil.d( "onFF_REWGesture: ly_VG.getWidth()" + getWidth());
                //根据移动的正负决定快进还是快退

                if (offset > 0) {
//                    scl.setImageResource(R.drawable.ff);
                    newProgress = (int) (offset/getWidth() * 100);
                    if (newProgress > 100){
                        newProgress = 100;
                    }
                }else {
//                    scl.setImageResource(R.drawable.fr);
                    newProgress = (int) (offset/getWidth() * 100);
                    if (newProgress < -100){
                        newProgress = -100;
                    }
                }
//                scl.setProgress(newProgress);
//                scl.show();
            }

            //点击事件
            @Override
            public void onSingleTapGesture(MotionEvent e) {

            }

            //双击事件
            @Override
            public void onDoubleTapGesture(MotionEvent e) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }

            @Override
            public void onDown(MotionEvent e) {
                //每次按下的时候更新当前亮度和音量，还有进度
//                oldProgress = newProgress;
                oldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                if (brightnessChangeListener != null) {
//                    brightness = brightnessChangeListener.getBrightness();
//                }
//                if (brightnessChangeListener.getBrightness() == -1){
//                    //一开始是默认亮度的时候，获取系统亮度，计算比例值
//                    brightness =
//                }
            }

            @Override
            public void onEndFF_REW(MotionEvent e) {
                LogUtil.d("设置进度为" + newProgress);
                int currentPosition = videoView.getCurrentPosition();
                int duration = videoView.getDuration();

                int residue = duration - currentPosition;
                if (residue > 0) {
                    int msec = residue * Math.abs(newProgress) / 100;
                    LogUtil.d("设置进度为  " + (newProgress + msec));
                    if (newProgress > 0) {
                        videoView.seekTo(currentPosition+msec);
                    } else {
                        videoView.seekTo(currentPosition - msec);
                    }
                }
            }
        });

       diUpdate= Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        updateTime(tvTimeCurrent, videoView.getCurrentPosition());
                        updateTime(tvTimeTotal, videoView.getDuration());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * 展示状态
     * @param format
     */
    private void showStatus(String format) {
        llStatus.setVisibility(VISIBLE);
        tvStatus.setText(format);
        RxUtil.closeDisposable(diAutoHide);

        diAutoHide= Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (llStatus!=null) {
                            llStatus.setVisibility(GONE);
                        }
                    }
                });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到手机屏幕的宽和高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）
        //最大限度的展示宽和高
        int width = getDefaultSize(screenWidth, widthMeasureSpec);
        int height = getDefaultSize(screenHeight, heightMeasureSpec);

        setMeasuredDimension(width, 800);

    }

    public void setVideoGestureListener(VideoGestureListener videoGestureListener) {
        mVideoGestureListener = videoGestureListener;
    }

    public interface BackListener {
        void back();
    }


    public class VideoPlayerOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        private VideoPlayControlView videoGestureRelativeLayout;

        public VideoPlayerOnGestureListener(VideoPlayControlView videoGestureRelativeLayout) {
            this.videoGestureRelativeLayout = videoGestureRelativeLayout;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            LogUtil.d("onDown: ");
            hasFF_REW = false;
            //每次按下都重置为NONE
            mScrollMode = NONE;
            if (mVideoGestureListener != null) {
                mVideoGestureListener.onDown(e);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtil.d("onScroll: e1:" + e1.getX() + "," + e1.getY());
            LogUtil.d("onScroll: e2:" + e2.getX() + "," + e2.getY());
            LogUtil.d("onScroll: X:" + distanceX + "  Y:" + distanceY);
            switch (mScrollMode) {
                case NONE:
                    LogUtil.d("NONE: ");
                    //offset是让快进快退不要那么敏感的值
                    if (Math.abs(distanceX) - Math.abs(distanceY) > offsetX) {
                        mScrollMode = FF_REW;
                    } else {
                        if (e1.getX() < getWidth() / 2) {
                            mScrollMode = BRIGHTNESS;
                        } else {
                            mScrollMode = VOLUME;
                        }
                    }
                    break;
                case VOLUME:
                    if (mVideoGestureListener != null) {
                        mVideoGestureListener.onVolumeGesture(e1, e2, distanceX, distanceY);
                    }
                    LogUtil.d("VOLUME: ");
                    break;
                case BRIGHTNESS:
                    if (mVideoGestureListener != null) {
                        mVideoGestureListener.onBrightnessGesture(e1, e2, distanceX, distanceY);
                    }
                    LogUtil.d("BRIGHTNESS: ");
                    break;
                case FF_REW:
                    if (mVideoGestureListener != null) {
                        mVideoGestureListener.onFF_REWGesture(e1, e2, distanceX, distanceY);
                    }
                    hasFF_REW = true;
                    LogUtil.d("FF_REW: ");
                    break;
            }
            return true;
        }


        @Override
        public boolean onContextClick(MotionEvent e) {
            LogUtil.d("onContextClick: ");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtil.d("onDoubleTap: ");
            if (mVideoGestureListener != null) {
                mVideoGestureListener.onDoubleTapGesture(e);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LogUtil.d("onLongPress: ");
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            LogUtil.d("onDoubleTapEvent: ");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtil.d("onSingleTapUp: ");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtil.d("onFling: ");
            return super.onFling(e1, e2, velocityX, velocityY);
        }


        @Override
        public void onShowPress(MotionEvent e) {
            LogUtil.d("onShowPress: ");
            super.onShowPress(e);
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            LogUtil.d("onSingleTapConfirmed: ");
            if (mVideoGestureListener != null) {
                mVideoGestureListener.onSingleTapGesture(e);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * 亮度改变监听
     */
    public interface BrightnessChangeListener {
        void changeBrightness(float brightness);
        float getBrightness();
    }

    /**
     * 用于提供给外部实现的视频手势处理接口
     */
    public interface VideoGestureListener {
        //亮度手势，手指在Layout左半部上下滑动时候调用
        void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        //音量手势，手指在Layout右半部上下滑动时候调用
        void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        //快进快退手势，手指在Layout左右滑动的时候调用
        void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        //单击手势，确认是单击的时候调用
        void onSingleTapGesture(MotionEvent e);

        //双击手势，确认是双击的时候调用
        void onDoubleTapGesture(MotionEvent e);

        //按下手势，第一根手指按下时候调用
        void onDown(MotionEvent e);

        //快进快退执行后的松开时候调用
        void onEndFF_REW(MotionEvent e);
    }

    /**
     * 时间的格式化并更新时间
     *
     * @param textView
     * @param millisecond
     */
    public void updateTime(TextView textView, int millisecond) {
        int second = millisecond / 1000; //总共换算的秒
        int hh = second / 3600;  //小时
        int mm = second % 3600 / 60; //分钟
        int ss = second % 60; //时分秒中的秒的得数

        String str = null;
        if (hh != 0) {
            //如果是个位数的话，前面可以加0  时分秒
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(str);
    }




}
