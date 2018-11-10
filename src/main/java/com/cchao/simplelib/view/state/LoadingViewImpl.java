package com.cchao.simplelib.view.state;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.ILoadingView;

/**
 * Description: loading View
 *
 * @author cchao
 * @version 2017/8/4
 */

public class LoadingViewImpl extends RelativeLayout implements ILoadingView {

    ImageView mImageView;
    ProgressBar mProgressBar;

    public LoadingViewImpl(Context context) {
        this(context, null);
    }

    public LoadingViewImpl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgressBar = new ProgressBar(context);
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
