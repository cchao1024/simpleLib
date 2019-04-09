package com.cchao.simplelib.ui.interfaces;

import android.support.annotation.StringRes;

import com.cchao.simplelib.core.UiHelper;

public interface BaseView {

    void showError();

    void showText(String string);

    default void showText(@StringRes int string) {
        showText(UiHelper.getString(string));
    }

    void showProgress(String string);

    void showProgress();

    void hideProgress();

}
