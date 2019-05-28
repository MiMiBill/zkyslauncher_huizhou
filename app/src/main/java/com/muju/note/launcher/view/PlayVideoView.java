package com.muju.note.launcher.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

public class PlayVideoView extends VideoView {

    private PlayListener playListener;

    public PlayVideoView(Context context) {
        super(context);
    }

    public PlayVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlayVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    @Override
    public void start() {
        super.start();
        if (playListener != null) {
            playListener.start();
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (playListener != null) {
            playListener.pause();
        }
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        super.setOnCompletionListener(l);
    }

    public interface PlayListener{
        void start();

        void pause();
    }
}
