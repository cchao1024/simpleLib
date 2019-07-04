package com.cchao.simplelib.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.AndroidHelper;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.model.event.CommonEvent;
import com.cchao.simplelib.ui.interfaces.BaseView;
import com.cchao.simplelib.ui.interfaces.EventSubscriber;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description: 所有Fragment的基类，便于统一处理Fragment
 *
 * @author cchao
 * @version 2017/8/2
 */
public class BaseFragment extends Fragment implements BaseView, EventSubscriber {

    protected Context mContext;
    protected CompositeDisposable mDisposable = new CompositeDisposable();
    protected ProgressDialog mProgressDialog;
    protected LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.logEvent("Fragment onCreate", getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
        addSubscribe(RxBus.get().toObservable(this::onEvent));
    }

    @Override
    public void showError() {
        showText(R.string.network_error);
    }

    @Override
    public void showText(String string) {
        UiHelper.showToast(string);
    }

    @Override
    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    @Override
    public void showProgress(String msg) {
        if (AndroidHelper.isContextDestroyed(mContext) || (mProgressDialog != null && mProgressDialog.isShowing())) {
            return;
        }
        mProgressDialog = UiHelper.showProgress(mContext, msg);
    }

    @Override
    public void hideProgress() {
        UiHelper.dismissProgress(mProgressDialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.dispose();
        UiHelper.dismissProgress(mProgressDialog);
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
    public void onEvent(CommonEvent event) {
        // 子类复写
    }
}
