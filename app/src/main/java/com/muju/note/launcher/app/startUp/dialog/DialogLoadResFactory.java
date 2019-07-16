package com.muju.note.launcher.app.startUp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;

import com.dyhdyh.widget.loading.factory.DialogFactory;
import com.muju.note.launcher.R;


/**
 * 加载资源文件
 */
public class DialogLoadResFactory implements DialogFactory {

    private int layoutRes;

    public DialogLoadResFactory(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public DialogLoadResFactory setCallback(ClickCallback clickCallBack) {
        this.clickCallback = clickCallBack;
        return this;
    }

    ClickCallback clickCallback;

    public interface ClickCallback {
        void onClick(Dialog dialog, int type);
    }

    @Override
    public Dialog onCreateDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.DialogFullscreen);
        dialog.setContentView(layoutRes);
        return dialog;
    }

    @Override
    public void setMessage(Dialog dialog, CharSequence message) {

    }

    @Override
    public int getAnimateStyleId() {
        return 0;
    }
}
