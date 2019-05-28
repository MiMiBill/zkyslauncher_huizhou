package com.muju.note.launcher.util.gilde;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muju.note.launcher.base.LauncherApplication;

public class GlideUtil {

    /**
     *  加载网络图片
     * @param url
     * @param iv
     * @param defaultImg
     */
    public static void loadImg(String url, ImageView iv,int defaultImg){
        Glide.with(LauncherApplication.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(defaultImg).error(defaultImg))
                .into(iv);
    }

}
