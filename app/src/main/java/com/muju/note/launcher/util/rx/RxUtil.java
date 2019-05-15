package com.muju.note.launcher.util.rx;

import android.app.Dialog;

import io.reactivex.disposables.Disposable;

/**
 * Rx工具
 */
public class RxUtil {
    public static void closeDisposable(Disposable di) {
        if (di != null && !di.isDisposed()) {
            di.dispose();
        }
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog != null &&dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
