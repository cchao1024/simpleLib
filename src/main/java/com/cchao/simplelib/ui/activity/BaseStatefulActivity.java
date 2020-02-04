package com.cchao.simplelib.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

        View childViewRoot = mLayoutInflater.inflate(getLayout(), findViewById(R.id.root_linear), false);
        try {
            mDataBind = DataBindingUtil.bind(childViewRoot);
        } catch (IllegalArgumentException e) {
            // 非 dataBinding 布局
        }
        // 获取委托实现
        mDelegate = LibCore.getLibConfig().getStateViewDelegate(mContext, childViewRoot, new Runnable() {
            @Override
            public void run() {
                onLoadData();
            }
        });
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((ViewGroup) findViewById(R.id.root_linear)).addView(mDelegate.getRootViewGroup(), layoutParams);
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
