package com.cchao.simplelib.ui.interfaces.impl;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.cchao.simplelib.ui.interfaces.TitleBar;

/**
 * TitleBar的委托类，通过委托类 实现两种风格的 titleBar
 * （1. 自定义布局  2. Toolbar实现）
 * @author cchao
 * @version 2019/4/10.
 */
public class TitleBarDelegate implements TitleBar {

    public View getTitleBarView() {
        return null;
    }

    @Override
    public void setTitleText(String text) {

    }

    @Override
    public void setTitleBarVisible(boolean visible) {

    }

    @Override
    public void setBackActionVisible(boolean visible) {

    }

    @Override
    public View addTitleMenuItem(Drawable icon, View.OnClickListener listener) {
        return null;
    }
}
