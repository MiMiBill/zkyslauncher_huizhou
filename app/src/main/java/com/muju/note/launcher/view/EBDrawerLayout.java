package com.muju.note.launcher.view;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EBDrawerLayout extends DrawerLayout {
    public EBDrawerLayout(Context context) {
        this(context, null);
    }

    public EBDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EBDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            if (ev.getY() > 10 && ev.getY() < 300)
                return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
