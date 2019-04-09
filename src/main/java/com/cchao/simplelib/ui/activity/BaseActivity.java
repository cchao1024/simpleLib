package com.cchao.simplelib.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected CompositeDisposable mDisposable = new CompositeDisposable();
    protected Context mContext;
    protected ProgressDialog mProgressDialog;
    protected LayoutInflater mLayoutInflater;

    @Override
    protected void onResume() {
        super.onResume();
        // 记录页面跳转的足迹
        Logs.logEvent("Activity Resume", getClass().getSimpleName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void showText(String stringId) {
        UiHelper.showToast(stringId);
    }

    @Override
    public void showProgress() {
        showProgress("正在加载...");
    }

    @Override
    public void showProgress(String msg) {
        if (isDestroyed() || (mProgressDialog != null && mProgressDialog.isShowing())) {
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError() {
        showText(R.string.network_error);
    }

    /**
     * 添加订阅事件
     *
     * @param subscription s
     */
    protected void addSubscribe(Disposable subscription) {
        mDisposable.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}