package com.cchao.simplelib.view.state;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.INetErrorView;


/**
 * Description: 网络出错的View
 *
 * @author cchao
 * @version 2017/8/4
 */

public class NetErrorViewImpl extends LinearLayout implements INetErrorView {

    View mReloadButton;

    public NetErrorViewImpl(Context context) {
        this(context, null);
    }

    public NetErrorViewImpl(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetErrorViewImpl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_net_error, this, true);
        mReloadButton = findViewById(R.id.net_error_reload);
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void setReloadListener(OnClickListener onClickListener) {
        mReloadButton.setOnClickListener(onClickListener);
    }
}
