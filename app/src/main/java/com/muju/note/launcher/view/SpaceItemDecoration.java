package com.muju.note.launcher.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 添加间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int left;
    int right;
    int top;
    int bottom;

    public SpaceItemDecoration(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = left;
        if (!isLastColum(parent, parent.getChildLayoutPosition(view))) {
            outRect.right = right;
        }
        outRect.top = top;
        outRect.bottom = bottom;
    }

    //判断是最后一列
    private boolean isLastColum(RecyclerView parent, int pos) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            if ((pos + 1) % glm.getSpanCount() == 0) {
                return true;
            }
        }
        return false;
    }

}
