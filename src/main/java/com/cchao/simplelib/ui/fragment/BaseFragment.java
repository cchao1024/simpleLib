package com.cchao.simplelib.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.ui.interfaces.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description: 所有Fragment的基类，便于统一处理Fragment
 *
 * @author cchao
 * @version 2017/8/2
 */

public class BaseFragment extends Fragment implements BaseView {

    protected Context mContext;
    protected CompositeDisposable mDisposable = new CompositeDisposable();
    protected ProgressDialog mProgressDialog;
    protected LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.d("Fragment onCreate >>>" + getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
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
    public void showText(int stringRes) {
        showText(getString(stringRes));
    }

    @Override
    public void showProgress() {
        showProgress("正在加载...");
    }

    @Override
    public void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new ProgressDialog(mContext);
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
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.dispose();
    }

    /**
     * 添加订阅事件
     *
     * @param subscription s
     */
    protected void addSubscribe(Disposable subscription) {
        mDisposable.add(subscription);
    }
}
