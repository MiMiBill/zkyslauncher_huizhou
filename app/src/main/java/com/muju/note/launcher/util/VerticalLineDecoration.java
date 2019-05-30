package com.muju.note.launcher.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by anyrsan on 2017/12/13.
 */

public class VerticalLineDecoration extends RecyclerView.ItemDecoration {

    private int mVerticalSpacing = 0;

    private int mLastSpacing = 0;

    public boolean isFirst = false;

    public VerticalLineDecoration(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
    }

    public VerticalLineDecoration(int verticalSpacing, boolean isFirst) {
        mVerticalSpacing = verticalSpacing;
        this.isFirst = isFirst;
    }

    public VerticalLineDecoration(int verticalSpacing, boolean isFirst, int mLastSpacing) {
        mVerticalSpacing = verticalSpacing;
        this.isFirst = isFirst;
        this.mLastSpacing = mLastSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            outRect.top = mVerticalSpacing;
        } else {
            if (isFirst) {
                outRect.top = mVerticalSpacing;
            }
        }
        int lastPosition = parent.getAdapter().getItemCount();
        if (position == lastPosition - 1) {  //多预留空间
            outRect.bottom = mVerticalSpacing + mLastSpacing;
        }
    }


}
