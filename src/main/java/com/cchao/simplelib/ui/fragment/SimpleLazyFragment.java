package com.cchao.simplelib.ui.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.cchao.simplelib.core.Logs;


/**
 * Description: 懒加载Fragment
 *
 * @author cchao
 * @versi 2017/8/2
 */
public abstract class SimpleLazyFragment<B extends ViewDataBinding> extends BaseStatefulFragment<B> {

    private static final String TAG = "SimpleLazyFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (mIsPrepared) {
            onFirstUserVisible();
            Logs.d(TAG, "onFirstUserVisible");
        } else {
            mIsPrepared = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (mIsFirstInvisible) {
                mIsFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstResume) {
            mIsFirstResume = false;
            return;
        }

        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    protected void initEventAndData() {
        //由于是懒加载覆盖这个这个初始化  在懒加载onFirstUserVisible 再加载数据
    }

    /**
     * fragment first visible, to init
     */
    public abstract void onFirstUserVisible();

    /**
     * fragment可见(切换回来或者onResume)
     */
    public void onUserVisible() {
        Logs.d(TAG, "onUserVisible");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
        Logs.d(TAG, "onFirstUserInvisible");
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
        Logs.d(TAG, "onUserInvisible");
    }


    private boolean mIsFirstVisible = true;
    protected boolean mIsFirstInvisible = true;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean mIsFirstResume = true;
    private boolean mIsPrepared;    //fragment is created
}

