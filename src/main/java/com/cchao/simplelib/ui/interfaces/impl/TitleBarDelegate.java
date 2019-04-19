package com.cchao.simplelib.ui.interfaces.impl;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.core.CollectionHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.databinding.CommonTitleBarBinding;
import com.cchao.simplelib.databinding.CommonToolBarBinding;
import com.cchao.simplelib.ui.interfaces.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * TitleBar的委托类，通过委托类 实现两种风格的 titleBar
 * （1. 自定义布局  2. Toolbar实现）
 *
 * @author cchao
 * @version 2019/4/10.
 */
public class TitleBarDelegate implements TitleBar {
    Context mContext;
    /**
     * 1. 普通线性 title
     * 2. toolbar实现
     */
    Const.TitleStyle mStyle = LibCore.getLibConfig().getTitleBarStyle();

    CommonTitleBarBinding mTitleBinding;
    CommonToolBarBinding mToolBinding;
    Toolbar mToolbar;
    List<MenuBean> mMenuList = new ArrayList<>();
    // 通过预置的 多个 action menu。等待 应用层赋值
    int[] mMenuActionArr = {R.id.action_1, R.id.action_2, R.id.action_3, R.id.action_4, R.id.action_5, R.id.action_6};

    public TitleBarDelegate(Context context, ViewGroup parent) {
        mContext = context;
        switch (mStyle) {
            case title:
                mTitleBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext)
                    , R.layout.common_title_bar, parent, false);
                break;

            case ToolBar:
                mToolBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext)
                    , R.layout.common_tool_bar, parent, false);
                mToolbar = mToolBinding.toolbar;
                if (!(mContext instanceof AppCompatActivity)) {
                    return;
                }
                AppCompatActivity activity = ((AppCompatActivity) mContext);
                activity.setSupportActionBar(mToolbar);
                ActionBar actionBar = activity.getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                break;
        }
    }

    private View getRoot() {
        if (mStyle == Const.TitleStyle.title) {
            return mTitleBinding.getRoot();
        } else {
            return mToolBinding.getRoot();
        }
    }

    /**
     * activity的调用，对menu 初始化显示
     */
    public void onCreateOptionsMenu() {
        if (CollectionHelper.isEmpty(mMenuList)) {
            return;
        }
        // 显示 menu 项目
        for (int i = 0; i < mMenuList.size(); i++) {
            MenuBean bean = mMenuList.get(i);
            // 设置menuItem
            MenuItem menuItem = mToolbar.getMenu().findItem(bean.id);
            if (menuItem != null) {
                menuItem.setVisible(true);
            }
        }

        // 设置click
        mToolbar.setOnMenuItemClickListener(item -> {
            for (int i = 0; i < mMenuList.size(); i++) {
                MenuBean bean = mMenuList.get(i);
                if (item.getItemId() != bean.getId()) {
                    continue;
                }
                if (bean.getOnClickListener() != null) {
                    bean.getOnClickListener().onClick(item.getActionView());
                }
            }
            return true;
        });
    }

    public View getTitleBarView() {
        return getRoot();
    }

    @Override
    public void setTitleText(String text) {
        if (mStyle == Const.TitleStyle.title) {
            mTitleBinding.title.setText(text);
        } else {
            mToolbar.setTitle(text);
        }
    }

    @Override
    public void setTitleBarVisible(boolean visible) {
        UiHelper.setVisibleElseGone(getRoot(), visible);
    }

    @Override
    public void setBackActionVisible(boolean visible, View.OnClickListener listener) {
        if (mStyle == Const.TitleStyle.title) {
            UiHelper.setVisibleElseGone(mTitleBinding.back, visible);
            if (listener != null) {
                mTitleBinding.back.setOnClickListener(listener);
            }
        } else {
            // empty implement
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    @Override
    public View addTitleMenuItem(Drawable icon, View.OnClickListener listener) {
        if (mStyle == Const.TitleStyle.title) {
            ImageView imageView = (ImageView) LayoutInflater.from(mContext)
                .inflate(R.layout.common_title_menu_item, mTitleBinding.actionGroup, false);
            imageView.setImageDrawable(icon);
            this.addTitleMenuItem(imageView, listener);
            return imageView;
        } else {
            mMenuList.add(new MenuBean(getNextId(), icon, listener));
        }
        return null;
    }

    public int getNextId() {
        return mMenuActionArr[mMenuList.size()];
    }

    @Override
    public void addTitleMenuItem(View menuView, View.OnClickListener listener) {
        if (mStyle == Const.TitleStyle.title) {
            mTitleBinding.actionGroup.addView(menuView);
            menuView.setOnClickListener(listener);
        }
        // toolbar 暂未实现
    }

    public class MenuBean {
        int id;
        Drawable mDrawable;
        View.OnClickListener mOnClickListener;

        public MenuBean(int id, Drawable drawable, View.OnClickListener onClickListener) {
            this.id = id;
            mDrawable = drawable;
            mOnClickListener = onClickListener;
        }

        public int getId() {
            return id;
        }

        public Drawable getDrawable() {
            return mDrawable;
        }

        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }
    }
}
