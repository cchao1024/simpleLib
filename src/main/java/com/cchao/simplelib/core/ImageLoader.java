package com.cchao.simplelib.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.ThreadPoolUtils;

/**
 * 图片加载工具类  glide封装
 * Created by cchao on 2016/12/1.
 */

public class ImageLoader {

    private static final String TAG_LOG = "ImageLoader";

    public static final String CACHE_FOLDER = "cache";
    private static final String UPLOAD_FOLDER = "cache/upload";
    private static final String PROFILE_FOLDER = "profile";
    private static final @DrawableRes
    int PLACE_HOLDER_TRISECTION = 0;
    private static final @DrawableRes
    int PLACE_HOLDER_COMMON = 0;

    /**
     * 默认加载 1/3 占位图，下载图 fitCenter
     */
    public static void loadImageViewFit(Context context, String path, ImageView imageView) {
        loadImageViewFit(context, path, imageView, PLACE_HOLDER_TRISECTION);
    }

    /**
     * 默认加载 1/3 占位图，下载图 fitCenter
     */
    public static void loadImageViewFit(Context context, String path, ImageView imageView, @DrawableRes int placeholder) {
        if (isContextDestroyed(context)) {
            return;
        }
        GlideApp.with(context.getApplicationContext())
            .load(path)
            .placeholder(placeholder)
            .fitCenter()
            .into(imageView);
    }

    public static void loadImageCompat(Context context, String imgUrl, ImageView imageView, int width, int height) {
        if (isContextDestroyed(context)) {
            return;
        }
        try {
            if (height > width) {
                loadImageCrop(context, imgUrl, imageView, PLACE_HOLDER_TRISECTION);
            } else {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                GlideApp.with(context.getApplicationContext())
                    .load(imgUrl)
                    .placeholder(PLACE_HOLDER_TRISECTION)
                    .listener(getFitListener(imageView))
                    .into(imageView);
            }
        } catch (Exception e) {
            ExceptionCollect.logException(e);
        }
    }

    public static void loadImageCrop(Context context, String imgUrl, ImageView imageView) {
        loadImageCrop(context, imgUrl, imageView, PLACE_HOLDER_TRISECTION);
    }

    //加载gif
    public static void loadGifImage(Context context, @DrawableRes Integer resourceId, ImageView mImageView) {
        if (isContextDestroyed(context)) {
            return;
        }
        //加载本地的Gif 不能使用缓存，不然卡成狗
        GlideApp.with(context).asGif().load(resourceId).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().into(mImageView);
    }

    /**
     * CENTER_INSIDE 占位图，CENTER_CROP 下载图
     *
     * @param placeholder 占位图
     */
    public static void loadImageCrop(Context context, String imgUrl, ImageView imageView, @DrawableRes int placeholder) {
        if (isContextDestroyed(context)) {
            return;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        GlideApp.with(context)
            .load(imgUrl)
            .placeholder(placeholder)
            .listener(getCropListener(imageView))
            .into(imageView);
    }

    /**
     * 检查activity 是不是已经被destroy 了
     *
     * @return
     */
    private static boolean isContextDestroyed(Context context) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return true;
        }
        return false;
    }

    public static void clearCache(Context context) {
        try {
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context.getApplicationContext()).clearDiskCache();
                }
            });
        } catch (Exception e) {
            Logs.e(TAG_LOG, "clearCache error: ", e);
        }
    }

    private static <T> RequestListener<T> getCropListener(ImageView imageView) {
        return new RequestListener<T>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return false;
            }
        };
    }

    private static <T> RequestListener<T> getFitListener(ImageView imageView) {
        return new RequestListener<T>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return false;
            }
        };
    }

}