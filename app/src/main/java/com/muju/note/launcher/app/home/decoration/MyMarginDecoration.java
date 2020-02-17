package com.muju.note.launcher.app.home.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.muju.note.launcher.util.log.LogUtil;

public class MyMarginDecoration extends RecyclerView.ItemDecoration {

    private static boolean isHasSetMargin = false;
    private int margin;
    public MyMarginDecoration(Context context) {
        margin = 6;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int allSize = state.getItemCount();
        //LogUtil.d("getItemCount:" + allSize);

        int height =  parent.getMeasuredHeight();
        int itemHeight = view.getLayoutParams().height;
        int magin = (height - itemHeight * 2 )/2 - 0;
//        LogUtil.d("height:" + height + "  itemHeight:" + itemHeight + " magin:" + magin);
        margin = magin;
        int rowNum = (allSize % 2 == 0) ? ( allSize/2 ): (allSize/2 + 1);
        int curPosition = parent.getChildLayoutPosition(view);

        if ( curPosition < rowNum) {
            outRect.set(margin, 0, margin, margin);
        } else {
            outRect.set(margin, margin, margin, 0);
        }




    }

}
