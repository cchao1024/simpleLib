package com.cchao.simplelib.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.cchao.simplelib.core.GlideApp;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.util.StringHelper;

/**
 * databinging 的自定义 属性转化
 *
 * @author : cchao
 * @version 2019-04-17
 */
public class BindingAdapters {

    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (StringHelper.isNotEmpty(url)) {
            ImageLoader.loadImage(imageView, url, placeholderRes);
        }
    }

    @BindingAdapter(value = {"circleUrl", "placeholderRes"}, requireAll = false)
    public static void setImageCircleUri(ImageView imageView, String url, Drawable placeholderRes) {
        if (StringHelper.isNotEmpty(url)) {
            GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(placeholderRes)
                .circleCrop()
                .into(imageView);
        }
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}
