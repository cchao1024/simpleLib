package com.cchao.simplelib.view.state.field;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cchao.simplelib.R;

/**
 * @author cchao
 * @version 2018/5/26.
 */
public class EmptyViewImpl extends LinearLayout {

    TextView mTvEmpty;

    public EmptyViewImpl(Context context) {
        this(context, null);
    }

    public EmptyViewImpl(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewImpl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_empty, this, true);
        mTvEmpty = findViewById(R.id.empty_text);
    }

    public EmptyViewImpl setText(@StringRes int id) {
        mTvEmpty.setText(id);
        return this;
    }
}
