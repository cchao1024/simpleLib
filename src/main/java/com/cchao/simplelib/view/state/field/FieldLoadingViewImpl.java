package com.cchao.simplelib.view.state.field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.ILoadingView;

/**
 * Description: loading View
 *
 * @author cchao
 * @version 2017/8/4
 */

public class FieldLoadingViewImpl extends FrameLayout implements ILoadingView {

    public FieldLoadingViewImpl(Context context) {
        this(context, null);
    }

    public FieldLoadingViewImpl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldLoadingViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_loading, this);
    }

    @Override
    public void showLoading() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideLoading() {
        setVisibility(GONE);
    }
}
