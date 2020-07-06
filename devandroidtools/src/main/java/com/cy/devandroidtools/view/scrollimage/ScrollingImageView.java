package com.cy.devandroidtools.view.scrollimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cy.devandroidtools.R;
import com.cy.devandroidtools.log.CLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.util.Collections.singletonList;

/**
 * 滚动的ImageView
 */
public class ScrollingImageView extends AppCompatImageView {
    public static ScrollingImageViewBitmapLoader BITMAP_LOADER = new ScrollingImageViewBitmapLoader() {
        @Override
        public Bitmap loadBitmap(Context context, int resourceId) {
            return BitmapFactory.decodeResource(context.getResources(), resourceId);
        }
    };

    private List<Bitmap> bitmaps;
    private float speed;
    private int[] scene;
    private int arrayIndex = 0;
    private int maxBitmapHeight = 0;

    private Rect clipBounds = new Rect();
    private float offset = 0;

    private boolean isStarted;

    private int sceneLength;
    private final int randomnessResourceId;
    private int[] randomness;
    private int resourceId;
    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
        int initialState = 0;
        try {
            initialState = ta.getInt(R.styleable.ParallaxView_initialState, 0);
            speed = ta.getDimension(R.styleable.ParallaxView_speed, 10);
            sceneLength = ta.getInt(R.styleable.ParallaxView_sceneLength, 1000);
            randomnessResourceId = ta.getResourceId(R.styleable.ParallaxView_randomness, 0);
            randomness = new int[0];
            if (randomnessResourceId > 0) {
                randomness = getResources().getIntArray(randomnessResourceId);
            }

            resourceId = ta.getResourceId(R.styleable.ParallaxView_src, 0);
        } finally {
            ta.recycle();
        }
    }

    private String[] showUrls ;
    public void scrollByUrls(Context context , String[] urls , int bitmapHeight){
        int bitmapH = Target.SIZE_ORIGINAL;
        if(bitmapHeight > 0){
            bitmapH = bitmapHeight;
        }
        CLog.println("scrollByUrl bitmapHeight="+bitmapH  +" parentH="+getHeight());

        if(urls == null || urls.length <= 0){
            return;
        }

        showUrls = urls;


        for(int i = 0 ; i < urls.length ; i ++){
            RequestBuilder<Bitmap> listener = Glide.with(context).asBitmap().load(urls[i]).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource != null) {
                        CLog.println("scrollByUrl<2> bitmapHeight="+resource.getHeight()  +" parentH="+getHeight());
                        if(bitmaps == null){
                            bitmaps = new ArrayList<>();
                        }
                        bitmaps.add(resource);
                        maxBitmapHeight = bitmaps.get(0).getHeight();
                        if(bitmapHeight > 0){
                            maxBitmapHeight = bitmapHeight;
                        }

                        if(bitmaps != null && bitmaps.size() == showUrls.length){
                            CLog.println("scrollByUrl<3> bitmaps="+bitmaps.size());
                            Random random = new Random();
                            scene = new int[bitmaps.size()];
                            for (int i = 0; i < scene.length; i++) {
//                                scene[i] = random.nextInt(bitmaps.size());
                                scene[i] = i;
                            }
                            start();
                        }

                    } else {
                        bitmaps = Collections.emptyList();
                        return false;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 加载网络图片
     * @param context
     * @param url 图片url
     * @param bitmapHeight 图片高度
     */
    public void scrollByUrl(Context context , String url , int bitmapHeight){
        int bitmapH = Target.SIZE_ORIGINAL;
        if(bitmapHeight > 0){
            bitmapH = bitmapHeight;
        }
        CLog.println("scrollByUrl bitmapHeight="+bitmapH  +" parentH="+getHeight());
        RequestBuilder<Bitmap> listener = Glide.with(context).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (resource != null) {
                    CLog.println("scrollByUrl<2> bitmapHeight="+resource.getHeight()  +" parentH="+getHeight());
                    bitmaps = singletonList(resource);
                    maxBitmapHeight = bitmaps.get(0).getHeight();
                    if(bitmapHeight > 0){
                        maxBitmapHeight = bitmapHeight;
                    }
                    scene = new int[]{0};

                } else {
                    bitmaps = Collections.emptyList();
                    return false;
                }
                start();
                return true;
            }
        });
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(!isInEditMode()) {
            super.onDraw(canvas);
            if (canvas == null || bitmaps == null || bitmaps.isEmpty()) {
                return;
            }

            canvas.getClipBounds(clipBounds);

            while (offset <= -getBitmap(arrayIndex).getWidth()) {
                offset += getBitmap(arrayIndex).getWidth();
                arrayIndex = (arrayIndex + 1) % scene.length;
            }

            float left = offset;
            for (int i = 0; left < clipBounds.width(); i++) {
                Bitmap bitmap = getBitmap((arrayIndex + i) % scene.length);
                int width = bitmap.getWidth();
                canvas.drawBitmap(bitmap, getBitmapLeft(width, left), 0, null);
                left += width;
            }

            if (isStarted && speed != 0) {
                offset -= abs(speed);
                postInvalidateOnAnimation();
            }
        }
    }

    private Bitmap getBitmap(int sceneIndex) {
        return bitmaps.get(scene[sceneIndex]);
    }

    private float getBitmapLeft(float layerWidth, float left) {
        if (speed < 0) {
            return clipBounds.width() - layerWidth - left;
        } else {
            return left;
        }
    }

    /**
     * Start the animation
     */
    public void start() {
        if (!isStarted) {
            isStarted = true;
            postInvalidateOnAnimation();
        }
    }

    /**
     * Stop the animation
     */
    public void stop() {
        if (isStarted) {
            isStarted = false;
            invalidate();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        if (isStarted) {
            postInvalidateOnAnimation();
        }
    }
}
