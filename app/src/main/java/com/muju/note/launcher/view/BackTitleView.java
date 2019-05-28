package com.muju.note.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;


/**
 * 统一的title
 * 可根据业务调整
 */
public class BackTitleView extends RelativeLayout {
    Button backBtn;
    TextView titleView;

    public BackTitleView(Context context) {
        super(context);
        init();
    }

    public BackTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BackTitleView);
        boolean titleCenter = attributes.getBoolean(R.styleable.BackTitleView_titleCenter, true);
        String title = attributes.getString(R.styleable.BackTitleView_title);
        attributes.recycle();
        if (attributes != null) {
            titleView.setText(title);
            LayoutParams layoutParams = (LayoutParams) titleView.getLayoutParams();
            layoutParams.addRule(titleCenter ? RelativeLayout.CENTER_IN_PARENT : RelativeLayout.CENTER_VERTICAL);
            titleView.setLayoutParams(layoutParams);
        }

    }

    public void setTitle(String title) {
        if (titleView == null) return;
        titleView.setText(title);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.back_title_layout, this, true);
        //在外部xml使用BackTitleView设置id时不能使用此次出现的id，否则回出现类型转换异常java.lang.ClassCastException
        backBtn = findViewById(R.id.back_btn);
        titleView = findViewById(R.id.title_view);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 这玩意只能用作activity的头部，不然会有问题，省却还要在activity中注册事件的麻烦，如果activity返回键有更多的逻辑可以从写finish
                ((Activity) getContext()).finishAfterTransition();
            }
        });
    }

    public void hideBackBtn() {
        backBtn.setVisibility(GONE);
    }


}
