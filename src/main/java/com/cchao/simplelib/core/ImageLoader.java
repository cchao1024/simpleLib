package com.cchao.simplelib.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cchao.simplelib.util.ThreadPoolUtils;

/**
 * 图片加载工具类  glide封装
 * Created by cchao on 2016/12/1.
 */

public class ImageLoader {

    private static final String TAG_LOG = "ImageLoader";

    public static void loadImage(Context context, String path, ImageView imageView) {
        if (isContextDestroyed(context)) {
            return;
        }
        GlideApp.with(context.getApplicationContext())
            .load(path)
            .placeholder(new ColorDrawable(Color.LTGRAY))
            .into(imageView);
    }

    public static void loadImage(Context context, String path, ImageView imageView
        , @DrawableRes int placeholder) {
        if (isContextDestroyed(context)) {
            return;
        }
        GlideApp.with(context.getApplicationContext())
            .load(path)
            .placeholder(placeholder)
            .fitCenter()
            .into(imageView);
    }

    //加载gif
    public static void loadGifImage(Context context, @DrawableRes Integer resourceId, ImageView mImageView) {
        if (isContextDestroyed(context)) {
            return;
        }

        GlideApp.with(context)
            .asGif()
            .load(resourceId)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .into(mImageView);
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

}