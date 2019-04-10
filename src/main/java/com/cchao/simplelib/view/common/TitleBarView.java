package com.cchao.simplelib.view.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.TitleBar;

/**
 * @author cchao
 * @version 2019/4/10.
 */
public class TitleBarView extends RelativeLayout implements TitleBar {
    ViewGroup mRoot;
    Context mContext;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

    }

    @Override
    public void setTitleText(String text) {
        mRoot = LayoutInflater.from(mContext).inflate(R.layout.title_bar, this);
    }

    @Override
    public void setTitleBarVisible(boolean visible) {

    }

    @Override
    public void setBackActionVisible(boolean visible) {

    }

    @Override
    public View addTitleMenuItem(Drawable icon, OnClickListener listener) {
        return null;
    }
}
