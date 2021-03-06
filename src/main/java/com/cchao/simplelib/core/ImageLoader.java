package com.cchao.simplelib.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.util.ThreadHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * 图片加载工具类  glide封装
 * Created by cchao on 2016/12/1.
 */
public class ImageLoader {

    private static final String TAG_LOG = "ImageLoader";

    public static void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, R.drawable.place_holder);
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

    public static void loadImage(ImageView imageView, Bitmap bitmap, @DrawableRes int placeholder) {
        if (AndroidHelper.isContextDestroyed(imageView.getContext())) {
            return;
        }

        GlideApp.with(imageView.getContext())
            .load(bitmap)
            .placeholder(placeholder)
            .fitCenter()
            .into(imageView);
    }

    public static void loadImageCircle(ImageView imageView, String path, @DrawableRes int placeholder) {
        if (AndroidHelper.isContextDestroyed(imageView.getContext())) {
            return;
        }

        GlideApp.with(imageView.getContext())
            .load(path)
            .placeholder(placeholder)
            .circleCrop()
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

    public static void loadVideoScreenshot(String uri, ImageView imageView, long frameTimeMicros) {
        if (AndroidHelper.isContextDestroyed(imageView.getContext())) {
            return;
        }

        Glide.with(imageView.getContext())
            .load(uri)
            .apply(RequestOptions.frameOf(frameTimeMicros)
                .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST))
            .transform(new BitmapTransformation() {
                @Override
                protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                    return toTransform;
                }

                @Override
                public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                    try {
                        messageDigest.update((LibCore.getContext().getPackageName() + "RotateTransform").getBytes("utf-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            })
            .into(imageView);
    }

    public static void loadVideoCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(imageView.getContext())
            .setDefaultRequestOptions(
                new RequestOptions()
                    .frame(0)
                    .centerCrop()
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
            )
            .load(url)
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

    /**
     * 下载图片，
     * 注意 manifest 配置 android:requestLegacyExternalStorage="true"
     */
    public static void downloadImageToLocalPicture(String url) {
        ThreadHelper.execute(() -> {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Logs.e("创建目录失败");
                }
            }
            File imageFile = new File(dir, "jpg_" + System.currentTimeMillis() + ".jpg");
            if (imageFile.exists()) {
                imageFile.delete();
            }
            File downloadFile = null;
            try {
                downloadFile = Glide.with(LibCore.getContext()).downloadOnly().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } catch (Exception e) {
                UiHelper.showToast(e.getMessage());
                e.printStackTrace();
                return;
            }
            copy(downloadFile, imageFile);
            UiHelper.showToast("图片下载完成");
        });
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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