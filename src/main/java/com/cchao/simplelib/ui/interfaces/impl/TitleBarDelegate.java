package com.cchao.simplelib.ui.interfaces.impl;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.databinding.CommonTitleBarBinding;
import com.cchao.simplelib.ui.interfaces.TitleBar;

/**
 * TitleBar的委托类，通过委托类 实现两种风格的 titleBar
 * （1. 自定义布局  2. Toolbar实现）
 *
 * @author cchao
 * @version 2019/4/10.
 */
public class TitleBarDelegate implements TitleBar {
    Context mContext;
    // 默认位自定义的布局
    int mStyle = 1;
    CommonTitleBarBinding mTitleBarBinding;

    public TitleBarDelegate(Context context) {
        mContext = context;
    }

    public View getTitleBarView() {
        if (mStyle == 1) {
            mTitleBarBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.common_title_bar, null, false);
            return mTitleBarBinding.getRoot();
        }
        return null;
    }

    @Override
    public void setTitleText(String text) {
        if (mStyle == 1) {
            mTitleBarBinding.title.setText(text);
        }
    }

    @Override
    public void setTitleBarVisible(boolean visible) {
        if (mStyle == 1) {
            UiHelper.setVisibleElseGone(mTitleBarBinding.getRoot(), visible);
        }
    }

    @Override
    public void setBackActionVisible(boolean visible, View.OnClickListener listener) {
        UiHelper.setVisibleElseGone(mTitleBarBinding.back, visible);
        if (listener != null) {
            mTitleBarBinding.back.setOnClickListener(listener);
        }
    }

    @Override
    public View addTitleMenuItem(Drawable icon, View.OnClickListener listener) {
        if (mStyle == 1) {
            ImageView imageView = (ImageView) LayoutInflater.from(mContext)
                .inflate(R.layout.common_title_menu_item, mTitleBarBinding.actionGroup, false);
            imageView.setImageDrawable(icon);
            this.addTitleMenuItem(imageView, listener);
            return imageView;
        }
        return null;
    }

    @Override
    public void addTitleMenuItem(View menuView, View.OnClickListener listener) {
        if (mStyle == 1) {
            mTitleBarBinding.actionGroup.addView(menuView);
            menuView.setOnClickListener(listener);
        }
    }
}
