package com.muju.note.launcher.app.startUp.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.WindowManager;

import com.muju.note.launcher.util.UIUtils;

import static com.dyhdyh.widget.loading.dialog.LoadingDialog.make;


/**
 * Dialog 工厂
 */
public class DialogFactory {
    public static void calculateParamsChoose(Dialog dialog, double width, double height){
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = (int) ((double) UIUtils.mScreenWidth * width); // 宽度设置为屏幕的0.65;
        params.height = (int) ((double) UIUtils.mScreenHeight *height); // 高度设置为屏幕的0.6 ;
        dialog.getWindow().setAttributes(params);
    }

    public static void calculateParamsChoosePad15(Dialog dialog){
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = UIUtils.mScreenWidth -UIUtils.dp2px(30); // 宽度设置为屏幕的0.65;
        dialog.getWindow().setAttributes(params);
    }
    public static void calculateParamsChooseMaxWidth(Dialog dialog){
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = UIUtils.mScreenWidth;
        dialog.getWindow().setAttributes(params);
    }

    public static void calculateParamsChoose(Dialog dialog){
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = (int) ((double) UIUtils.mScreenWidth * 0.824);
        params.height = (int) ((double)UIUtils.mScreenHeight *0.30);
        dialog.getWindow().setAttributes(params);
    }

    public static Dialog dialog(Context context, @LayoutRes int resId){
        Dialog dialog = make(context, new DialogLoadResFactory(resId)
        ).create();
        dialog.show();
        return dialog;
    }







}
