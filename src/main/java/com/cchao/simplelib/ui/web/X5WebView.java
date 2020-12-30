package com.cchao.simplelib.ui.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.viewpager.widget.ViewPager;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * 复写  x5webview
 *
 * @author cch
 * @version 2020/12/28
 */
public class X5WebView extends WebView {

    public X5WebView(Context context, boolean b) {
        super(context, b);
    }

    public X5WebView(Context context) {
        super(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (clampedX || clampedY) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private ViewParent findViewParentIfNeeds(ViewParent parent) {
        while (true) {
            if (parent == null) {
                return null;
            }
            if (parent instanceof ViewPager
                || parent instanceof AbsListView
                || parent instanceof ScrollView
                || parent instanceof HorizontalScrollView) {
                return parent;
            } else if (parent instanceof View) {
                parent = parent.getParent();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onTouchEvent(event);
    }

}
