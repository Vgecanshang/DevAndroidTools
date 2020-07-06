package com.cy.devandroidtools.img.loader;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cy.devandroidtools.img.base.ImageLoader;

/**
 * Glide图片加载器
 *
 * @author cy
 * @date 2020/5/14.
 */
public class GlideImageLoader implements ImageLoader {


    public static void displayImage(ImageView iv, Uri url) {
        Glide.with(iv.getContext()).load(url).into(iv);
    }

    public static void displayImage(ImageView iv, String url) {
        Glide.with(iv.getContext()).load(url).into(iv);
    }

    public void displayCircleImage(ImageView iv, String url) {
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }

    public void displayRoundCornerImage(ImageView iv, String url, int radius) {
        RequestOptions options = new RequestOptions().transform(new RoundedCorners(radius));
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }

    public static void displayNoCacheImage(ImageView iv, String url){
        RequestOptions options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }

    public static void displayWHImage(ImageView iv, String url , int width, int height){
        RequestOptions options = new RequestOptions().override(width , height);
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }

}
