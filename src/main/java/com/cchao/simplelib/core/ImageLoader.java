package com.cchao.simplelib.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.util.ThreadHelper;

/**
 * 图片加载工具类  glide封装
 * Created by cchao on 2016/12/1.
 */
public class ImageLoader {

    private static final String TAG_LOG = "ImageLoader";

    public static void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, R.drawable.title_back);
    }

    public static void loadImage(ImageView imageView, String path, @DrawableRes int placeholder) {
        if (AndroidHelper.isContextDestroyed(imageView.getContext())) {
            return;
        }

        GlideApp.with(imageView.getContext())
            .load(path)
            .placeholder(placeholder)
            .fitCenter()
            .into(imageView);
    }

    //加载gif
    public static void loadGifImage(@DrawableRes Integer resourceId, ImageView imageView) {
        if (AndroidHelper.isContextDestroyed(imageView.getContext())) {
            return;
        }

        GlideApp.with(imageView.getContext())
            .asGif()
            .load(resourceId)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .into(imageView);
    }

    /**
     * 下载图片，
     */
    public static void downloadImage(Context context, String url, RequestListener<Drawable> requestListener) {
        GlideApp.with(context)
            .load(url).listener(requestListener)
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    public static void clearCache(Context context) {
        try {
            ThreadHelper.execute(new Runnable() {
                @Override
                public void run() {
                    Glide.get(LibCore.getContext()).clearDiskCache();
                }
            });
        } catch (Exception e) {
            Logs.e(TAG_LOG, "clearCache error: ", e);
        }
    }
}