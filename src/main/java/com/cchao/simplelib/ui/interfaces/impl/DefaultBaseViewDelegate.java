package com.cchao.simplelib.ui.interfaces.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.AndroidHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseView;

/**
 * 实现默认的baseView功能 委托
 *
 * @author cchao
 * @version 2019-04-29.
 */
public class DefaultBaseViewDelegate implements BaseView {
    protected ProgressDialog mProgressDialog;
    protected Context mContext;

    public DefaultBaseViewDelegate(Context context) {
        mContext = context;

    }

    @Override
    public void showToast(String stringId) {
        UiHelper.showToast(stringId);
    }

    @Override
    public void showProgress() {
        showProgress(UiHelper.getString(R.string.loading));
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
    public void showError() {
        showToast(R.string.network_error);
    }
}
