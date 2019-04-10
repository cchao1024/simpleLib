package com.cchao.simplelib.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.INetErrorView;
import com.kennyc.view.MultiStateView;

/**
 * 具备状态切换的 Activity 基类,
 * 实现接口 {@link com.cchao.simplelib.ui.interfaces.BaseStateView}
 *
 * @author cchao
 * @version 2019/4/10.
 */
public abstract class BaseStatefulActivity<B extends ViewDataBinding> extends BaseActivity implements BaseStateView {
    MultiStateView mStateView;
    protected B mDataBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_state);

        mStateView = findViewById(R.id.state_layout);

        initStateView();
        initEventAndData();
    }

    protected abstract @LayoutRes
    int getLayout();

    /**
     * 对各事件和view 初始化
     */
    protected abstract void initEventAndData();

    /**
     * 加载数据，可被 net-error reload 按钮调起
     */
    protected abstract void onLoadData();

    //<editor-fold desc="对StateView的操作">
    private void initStateView() {
        View contentView = LayoutInflater.from(mContext).inflate(getLayout(), mStateView, false);
        try {
            mDataBind = DataBindingUtil.bind(contentView);
        } catch (Exception e) {
            Logs.logException(e);
        }
        mStateView.setViewForState(contentView, MultiStateView.VIEW_STATE_CONTENT);
        // 网络出错重新加载
        ((INetErrorView) mStateView.getView(MultiStateView.VIEW_STATE_ERROR))
            .setReloadListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchView(LOADING);
                    onLoadData();
                }
            });
    }

    @Override
    public void switchView(@BaseStateView.ViewType String viewType) {
        runOnUiThread(() -> {
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
    //</editor-fold>
}
