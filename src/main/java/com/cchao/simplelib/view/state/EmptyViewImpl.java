package com.cchao.simplelib.view.state;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.ILoadingView;
import com.kennyc.view.MultiStateView;

/**
 * Description: loading View
 *
 * @author cchao
 * @version 2017/8/4
 */

public class EmptyViewImpl extends RelativeLayout {

    public EmptyViewImpl(Context context) {
        this(context, null);
    }

    public EmptyViewImpl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_empty, this);
    }
}
