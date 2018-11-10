package com.cchao.simplelib.ui.interfaces;

import android.support.annotation.StringRes;

public interface BaseView {

    void showError();

    void showText(String string);

    void showText(@StringRes int string);

    void showProgress(String string);

    void showProgress();

    void hideProgress();

}
