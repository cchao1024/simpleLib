package com.cchao.simplelib.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.ui.interfaces.INetErrorView;
import com.cchao.simplelib.ui.interfaces.TitleBar;
import com.cchao.simplelib.ui.interfaces.impl.TitleBarDelegate;
import com.kennyc.view.MultiStateView;

/**
 * 标题栏抽象 实现接口 {@link TitleBar}
 * 通过复写 setContentView 插入一个 TitleBarView
 *
 * @author cchao
 * @version 2019/4/10.
 */
public abstract class BaseTitleBarActivity<B extends ViewDataBinding> extends BaseStatefulActivity implements TitleBar {
    TitleBarDelegate mTitleDelegate;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // 从委派类拿到 view
        LinearLayout rootLinear = findViewById(R.id.root_content_linear);
        rootLinear.addView(mTitleDelegate.getTitleBarView(), 0);
        initTitleBar();
    }

    @Override
    public void setTitleText(String text) {
        mTitleDelegate.setTitleText(text);
    }

    @Override
    public void setTitleBarVisible(boolean visible) {
        mTitleDelegate.setTitleBarVisible(visible);
    }

    @Override
    public void setBackActionVisible(boolean visible) {
        mTitleDelegate.setBackActionVisible(visible);
    }

    @Override
    public View addTitleMenuItem(Drawable icon, View.OnClickListener listener) {
        return mTitleDelegate.addTitleMenuItem(icon, listener);
    }
}
