package com.cchao.simplelib.view.state;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.UiHelper;
import com.kennyc.view.MultiStateView;

/**
 * 通用的多状态视图，网络出错，加载，
 *
 * @author cchao
 * @version 2018.2.7.
 */
public class CommonStateLayout extends MultiStateView {

    Context mContext;
    LoadingViewImpl mLoadingView;
    NetErrorViewImpl mNetErrorView;
    View mEmptyView;
    View mContentView;

    public CommonStateLayout(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public CommonStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    public CommonStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews();
    }

    private void initViews() {
        mLoadingView = new LoadingViewImpl(mContext);
        mNetErrorView = new NetErrorViewImpl(mContext);
        mContentView = new View(mContext);
        mEmptyView = new EmptyViewImpl(mContext);
        setViewForState(mContentView, MultiStateView.VIEW_STATE_CONTENT);
        setViewForState(mLoadingView, MultiStateView.VIEW_STATE_LOADING);
        setViewForState(mNetErrorView, MultiStateView.VIEW_STATE_ERROR);
        setViewForState(mEmptyView, MultiStateView.VIEW_STATE_EMPTY);
    }
}
