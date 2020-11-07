package com.cchao.simplelib.ui.interfaces;

import androidx.annotation.StringRes;

import com.cchao.simplelib.core.UiHelper;

public interface BaseView {

    void showError();

    void showToast(String string);

    default void showToast(@StringRes int string) {
        showToast(UiHelper.getString(string));
    }

    void showProgress(String string);

    void showProgress();

    void hideProgress();

}
