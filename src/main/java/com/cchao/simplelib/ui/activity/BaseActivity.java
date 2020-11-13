package com.cchao.simplelib.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.model.event.CommonEvent;
import com.cchao.simplelib.ui.interfaces.BaseView;
import com.cchao.simplelib.ui.interfaces.EventSubscriber;
import com.cchao.simplelib.util.LanguageUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, EventSubscriber {

    protected CompositeDisposable mDisposable = new CompositeDisposable();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    BaseView mDelegate;

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
        mDelegate = LibCore.getLibConfig().getBaseViewDelegate(mContext);
        addSubscribe(RxBus.get().toObservable(this::onEvent));
    }

    //<editor-fold desc="委托实现">
    @Override
    public void showToast(String stringId) {
        mDelegate.showToast(stringId);
    }

    @Override
    public void showProgress() {
        mDelegate.showProgress();
    }

    @Override
    public void showProgress(String msg) {
        mDelegate.showProgress(msg);
    }

    @Override
    public void hideProgress() {
        mDelegate.hideProgress();

    }

    @Override
    public void showError() {
        mDelegate.showError();
    }
    //</editor-fold>

    /**
     * 添加订阅事件
     *
     * @param subscription s
     */
    public void addSubscribe(Disposable subscription) {
        mDisposable.add(subscription);
    }

    @Override
    public void onEvent(CommonEvent event) {/*子类复写实现*/}

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}