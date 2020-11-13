package com.cchao.simplelib.ui.activity;

import androidx.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.TitleBar;
import com.cchao.simplelib.ui.interfaces.impl.DefaultTitleBarDelegate;

/**
 * 标题栏抽象 实现接口 {@link TitleBar}
 * 通过复写 setContentView 插入一个 TitleBarView
 *
 * @author cchao
 * @version 2019/4/10.
 */
public abstract class BaseTitleBarActivity<B extends ViewDataBinding> extends BaseStatefulActivity<B> implements TitleBar {
    DefaultTitleBarDelegate mTitleDelegate;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewGroup viewGroup = findViewById(R.id.root_linear);

        // 从委派类拿到 view
        mTitleDelegate = LibCore.getLibConfig().getTitleBarDelegate(mContext, viewGroup);
        mTitleDelegate.setBackActionVisible(true, v -> onBackPressed());
        // 添加到最顶部
        viewGroup.addView(mTitleDelegate.getTitleBarView(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        mTitleDelegate.onCreateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
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
    public void setBackActionVisible(boolean visible, View.OnClickListener onClickListener) {
        mTitleDelegate.setBackActionVisible(visible, onClickListener);
    }

    @Override
    public View addTitleMenuItem(Drawable icon, View.OnClickListener listener) {
        return mTitleDelegate.addTitleMenuItem(icon, listener);
    }

    @Override
    public void addTitleMenuItem(View icon, View.OnClickListener listener) {
        mTitleDelegate.addTitleMenuItem(icon, listener);
    }
}
