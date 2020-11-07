package com.cchao.simplelib.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.INetErrorView;
import com.kennyc.view.MultiStateView;

/**
 * Description: 具备状态切换的 Fragment 基类
 *
 * @author cchao
 * @version 2017/8/2
 */
public abstract class BaseStatefulFragment<B extends ViewDataBinding> extends BaseFragment
    implements BaseStateView {

    View mRootLinear;
    protected MultiStateView mStateView;
    protected B mDataBind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootLinear = inflater.inflate(R.layout.common_state, container, false);
        mStateView = mRootLinear.findViewById(R.id.state_layout);

        initStateView();
        return mRootLinear;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEventAndData();
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initEventAndData();

    /**
     * 加载数据，可被 net-error reload 按钮调起
     */
    protected abstract void onLoadData();

    //<editor-fold desc="对StateView的操作">
    private void initStateView() {
        View contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), mStateView, false);
        try {
            mDataBind = DataBindingUtil.bind(contentView);
        } catch (Exception ex) {
            showToast(" View is not a binding layout");
        }
        mStateView.setViewForState(contentView, MultiStateView.VIEW_STATE_CONTENT);
        // 网络出错重新加载
        ((INetErrorView) mStateView.getView(MultiStateView.VIEW_STATE_ERROR))
            .setReloadListener(v -> {
                switchView(LOADING);
                onLoadData();
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
        return null;
    }

    //</editor-fold>
}
