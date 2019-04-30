package com.cchao.simplelib.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.BaseStateView;

/**
 * 具备状态切换的 Activity 基类,
 * 实现接口 {@link com.cchao.simplelib.ui.interfaces.BaseStateView}
 *
 * @author cchao
 * @version 2019/4/10.
 */
public abstract class BaseStatefulActivity<B extends ViewDataBinding> extends BaseActivity implements BaseStateView {
    protected B mDataBind;
    BaseStateView mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_linear);

        mDataBind = DataBindingUtil.inflate(mLayoutInflater, getLayout(), null, false);
        // 获取委托实现
        mDelegate = LibCore.getLibConfig().getStateViewDelegate(mContext, mDataBind.getRoot(), new Runnable() {
            @Override
            public void run() {
                onLoadData();
            }
        });
        ((ViewGroup) findViewById(R.id.root_content_linear)).addView(mDelegate.getRootViewGroup());
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

    @Override
    public ViewGroup getRootViewGroup() {
        return mDelegate.getRootViewGroup();
    }

    @Override
    public void switchView(@BaseStateView.ViewType String viewType) {
        mDelegate.switchView(viewType);
    }
}
