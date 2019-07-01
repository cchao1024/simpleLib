package com.cchao.simplelib.view.state.field;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.UiHelper;
import com.kennyc.view.MultiStateView;

/**
 * 一小块域 的加载 刷新
 *
 * @author cchao
 * @version 2018/9/13.
 */
public class FieldStateLayout extends MultiStateView {
    protected Context mContext;
    public FieldLoadingViewImpl mLoadingView;
    public FieldNetErrorViewImpl mNetErrorView;
    View mEmptyView;
    int mFieldHeight;

    public FieldStateLayout(Context context) {
        this(context, null);
    }

    public FieldStateLayout(Context context, int fieldHeight) {
        this(context, null);
        mFieldHeight = fieldHeight;
    }

    public FieldStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.FieldStateLayout);
        mFieldHeight = attr.getDimensionPixelSize(R.styleable.FieldStateLayout_fieldHeight, UiHelper.dp2px(180));
        attr.recycle();

        mLoadingView = new FieldLoadingViewImpl(mContext);
        mNetErrorView = new FieldNetErrorViewImpl(mContext);
        mEmptyView = getView(MultiStateView.VIEW_STATE_EMPTY);
        if (mEmptyView == null) {
            mEmptyView = new EmptyViewImpl(mContext);
        }
        if (getView(MultiStateView.VIEW_STATE_CONTENT) == null) {
            setViewForState(new View(mContext), MultiStateView.VIEW_STATE_CONTENT);
        }
        setViewForState(mLoadingView, MultiStateView.VIEW_STATE_LOADING);
        setViewForState(mNetErrorView, MultiStateView.VIEW_STATE_ERROR);
        setViewForState(mEmptyView, MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void setViewState(int state) {
        super.setViewState(state);
        // 加载出错 默认 180dp
        if (getLayoutParams() != null) {
            if (state != MultiStateView.VIEW_STATE_CONTENT) {
                getLayoutParams().height = mFieldHeight;
            } else {
                getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        if (state == MultiStateView.VIEW_STATE_LOADING) {
            mLoadingView.showLoading();
        } else {
            mLoadingView.hideLoading();
        }
    }

    public void setReloadListener(OnClickListener onClickListener) {
        if (mNetErrorView == null) {
            return;
        }
        mNetErrorView.setReloadListener(onClickListener);
    }
}
