package com.cy.devandroidtools.img.options;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import java.util.List;

/**
 * class
 *  RequestOptions options = new RequestOptions();
 *         //options.format(DecodeFormat.PREFER_ARGB_8888)
 *         //options.centerCrop()//图片显示类型
 *         //options.placeholder(loadingRes)//加载中图片
 *         //options.error(errorRes)//加载错误的图片
 *         //options.error(new ColorDrawable(Color.RED))//或者是个颜色值
 *         //options.priority(Priority.HIGH)//设置请求优先级
 *         //options.diskCacheStrategy(DiskCacheStrategy.ALL);
 *         //options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)//仅缓存原图片
 *         //options.diskCacheStrategy(DiskCacheStrategy.ALL)//全部缓存
 *         //options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)缓存缩略过的
 *         //options.onlyRetrieveFromCache(true)//仅从缓存加载
 *         //options.skipMemoryCache(true)//禁用缓存,包括内存和磁盘
 *         //options.diskCacheStrategy(DiskCacheStrategy.NONE)仅跳过磁盘缓存
 *         //options.override(200,200)加载固定大小的图片
 *         //options.dontTransform()不做渐入渐出的转换
 *         //options.transition(new DrawableTransitionOptions().dontTransition())//同上
 *         //options.circleCrop()设置成圆形头像<这个是V4.0新增的>
 *         //options.transform(new RoundedCorners(10))设置成圆角头像<这个是V4.0新增的>
 * @author cy
 * @date 2020/5/14.
 */
public class GlideRequestOptions {
    /** 加载展位图 */
    private int placeholder = -1 ;
    /** 加载错误图 */
    private int errorDrawable = -1;
    /** 图片加载动画 */
    private  ViewPropertyTransition.Animator animator = null;
    /** 图片转换 (Glide RequestOptions.transform 可以处理多个图片转换)*/
    private Transformation<Bitmap> bitmapTransformation = null;
    /** 是否跳过内存缓存 */
    private  boolean isSkipMemoryCache = false; //
    /** 加载策略，如果不想缓存在磁盘上则设置DiskCacheStrategy.NONE */
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
    /** 加载制定大小的图片 */
    private ImageReSize size;
    /** 是否是圆形图片 v4.0新增的 */
    private boolean isCircle = false;

    public GlideRequestOptions(int placeholder, int errorDrawable, ViewPropertyTransition.Animator animator, Transformation<Bitmap> bitmapTransformation, boolean isSkipMemoryCache, DiskCacheStrategy diskCacheStrategy , ImageReSize size , boolean isCircle) {
        this.placeholder = placeholder;
        this.errorDrawable = errorDrawable;
        this.animator = animator;
        this.bitmapTransformation = bitmapTransformation;
        this.isSkipMemoryCache = isSkipMemoryCache;
        this.diskCacheStrategy = diskCacheStrategy;
        this.size = size;
        this.isCircle = isCircle;
    }

    public static final class Builder {
        /** 加载展位图 */
        private int placeholder = -1 ;
        /** 加载错误图 */
        private int errorDrawable = -1;
        /** 图片加载动画 */
        private  ViewPropertyTransition.Animator animator = null;
        /** 图片转换 (Glide RequestOptions.transform 可以处理多个图片转换)*/
        private Transformation<Bitmap> bitmapTransformation = null;
        /** 是否跳过内存缓存 */
        private  boolean isSkipMemoryCache = false; //
        /** 加载策略，如果不想缓存在磁盘上则设置DiskCacheStrategy.NONE */
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
        /** 加载制定大小的图片 */
        private ImageReSize size;
        /** 是否是圆形图片 v4.0新增的 */
        private boolean isCircle = false;

        public Builder() {

        }

        public Builder placeHolder(int drawable) {
            this.placeholder = drawable;
            return this;
        }

        public Builder reSize(ImageReSize size) {
            this.size = size;
            return this;
        }

        public Builder anmiator(ViewPropertyTransition.Animator animator) {
            this.animator = animator;
            return this;
        }

        public Builder errorDrawable(int errorDrawable) {
            this.errorDrawable = errorDrawable;
            return this;
        }

        public Builder isSkipMemoryCache(boolean isSkipMemoryCache) {
            this.isSkipMemoryCache = isSkipMemoryCache;
            return this;
        }

        public Builder transform(Transformation<Bitmap> transformation){
            this.bitmapTransformation = transformation;
            return this;
        }

        public Builder setDiskCacheStrategy(DiskCacheStrategy diskCacheStrategy){
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }

        public GlideRequestOptions build() {
            return new GlideRequestOptions(this.placeholder, this.errorDrawable, this.animator, this.bitmapTransformation, this.isSkipMemoryCache, this.diskCacheStrategy , this.size,this.isCircle);
        }



    }


}
