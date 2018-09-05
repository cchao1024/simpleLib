package com.cchao.simplelib.ui.activity;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.INetErrorView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 封装toolbar操作、状态切换的baseActivity
 * Created by cchao on 2017/3/16.
 */

public abstract class BaseToolbarActivity<B extends ViewDataBinding> extends BaseActivity implements BaseStateView {
    protected Toolbar mToolbar;
    MultiStateView mStateView;
    protected B mDataBind;

    private @StringRes
    int mActionText; //toolbar右边的文字
    List<Integer> mMenuItemIDs = new ArrayList<>(); // toolbar menu id 集合
    List<View.OnClickListener> mActionClickListeners = new ArrayList<>(); //toolbar右侧action事件
    protected Unbinder mUnBinder;
    protected Dialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_toolbar_activity);
        if (LibCore.getInfo().supportDatabinding()) {
        }
        mToolbar = findViewById(R.id.toolbar);
        mStateView = findViewById(R.id.state_layout);

        initToolbar();
        initStateView();
        // initState调起 getLayout set到Content，butterKnife要在其之后
        mUnBinder = ButterKnife.bind(this);
        initEventAndData();
    }

    protected abstract @LayoutRes
    int getLayout();

    /**
     * 对各事件的初始化
     */
    protected abstract void initEventAndData();

    /**
     * 加载数据，可被 net-error reload 按钮调起
     */
    protected abstract void onLoadData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //<editor-fold desc="对Toolbar的操作">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //使用通用的menuItem布局
        /*getMenuInflater().inflate(R.menu.common_menu, menu);
        for (int i = 0; i < mMenuItemIDs.size(); i++) {
            int mMenuItemID = mMenuItemIDs.get(i);
            //设置menuItem
            if (mMenuItemID != 0 && mToolbar.getMenu().findItem(mMenuItemID) != null) {
                mToolbar.getMenu().findItem(mMenuItemID).setVisible(true);

            }
        }
        //设置click
        mToolbar.setOnMenuItemClickListener(item -> {
            for (int i = 0; i < mMenuItemIDs.size(); i++) {
                int mMenuItemID = mMenuItemIDs.get(i);
                View.OnClickListener mActionClickListener = mActionClickListeners.get(i);
                if (item.getItemId() == mMenuItemID && mActionClickListener != null) {
                    mActionClickListener.onClick(item.getActionView());
                }
            }
            return true;
        });
        if (mActionText != 0) {
            mToolbar.getMenu().findItem(R.id.action_text).setTitle(mActionText);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    /**
     * 设置menu的item的点击事件
     *
     * @param id              menu id
     * @param onClickListener click Action
     */
    protected void addToolbarMenuItemClick(@IdRes int id, View.OnClickListener onClickListener) {
        mMenuItemIDs.add(id);
        mActionClickListeners.add(onClickListener);

    }

    protected void setToolBarText(@StringRes int title) {
//        mTitleTextView.setText(getString(title));
        mToolbar.setTitle(getString(title));
    }

    protected void setActionText(@StringRes int title) {
        mActionText = title;
    }

    protected void setToolBarText(String title) {
//        mTitleTextView.setText(title);
        mToolbar.setTitle(title);
    }

    protected void setToolBarAble(boolean able) {
        mToolbar.setVisibility(able ? View.VISIBLE : View.GONE);
    }
    //</editor-fold>

    //<editor-fold desc="对StateView的操作">
    private void initStateView() {
        View contentView= LayoutInflater.from(mContext).inflate(getLayout(),mStateView,false);
        mDataBind = DataBindingUtil.bind(contentView);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
            }});
    }
    //</editor-fold>
}
