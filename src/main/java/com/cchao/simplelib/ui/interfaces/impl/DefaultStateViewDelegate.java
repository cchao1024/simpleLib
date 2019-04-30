package com.cchao.simplelib.ui.interfaces.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.INetErrorView;
import com.cchao.simplelib.view.state.CommonStateLayout;
import com.kennyc.view.MultiStateView;

/**
 * @author cchao
 * @version 2019-04-29.
 */
public class DefaultStateViewDelegate extends DefaultBaseViewDelegate implements BaseStateView {
    CommonStateLayout mStateView;
    View mChildContent;
    Runnable mRetryCallBack;

    public DefaultStateViewDelegate(Context context, View childContent, Runnable retryCallBack) {
        super(context);
        mStateView = new CommonStateLayout(mContext);
        mChildContent = childContent;
        mRetryCallBack = retryCallBack;
        initStateView();
    }

    private void initStateView() {
        mStateView.setViewForState(mChildContent, MultiStateView.VIEW_STATE_CONTENT);
        // 网络出错重新加载
        ((INetErrorView) mStateView.getView(MultiStateView.VIEW_STATE_ERROR))
            .setReloadListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchView(LOADING);
                    mRetryCallBack.run();
                }
            });
    }

    @Override
    public void switchView(@BaseStateView.ViewType String viewType) {
        UiHelper.runOnUiThread(() -> {
            switch (viewType) {
                case LOADING:
                    mStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    break;
                case NET_ERROR:
                    mStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    break;
                case EMPTY:
                    mStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    break;
                case CONTENT:
                    mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    break;
            }
        });
    }

    @Override
    public ViewGroup getRootViewGroup() {
        return mStateView;
    }
}
