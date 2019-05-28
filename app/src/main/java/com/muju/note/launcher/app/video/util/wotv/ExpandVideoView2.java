package com.muju.note.launcher.app.video.util.wotv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.muju.note.launcher.app.video.event.VideoPauseEvent;
import com.muju.note.launcher.app.video.event.VideoStartEvent;
import com.unicom.common.base.video.expand.ExpandVideoView;

import org.greenrobot.eventbus.EventBus;

public class ExpandVideoView2 extends ExpandVideoView {

    public ExpandVideoView2(Context context) {
        super(context);
    }

    public ExpandVideoView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ExpandVideoView2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void pause() {
        super.pause();
        EventBus.getDefault().post(new VideoPauseEvent(true));
    }

    @Override
    public void start() {
        super.start();
        EventBus.getDefault().post(new VideoStartEvent(true));
    }
}

