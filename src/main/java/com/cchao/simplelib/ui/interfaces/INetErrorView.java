package com.cchao.simplelib.ui.interfaces;

import android.view.View;

/**
 * Description:
 *
 * @author cchao
 * @version 2017/8/4
 */

public interface INetErrorView {
    void show();

    void hide();

    void setReloadListener(View.OnClickListener onClickListener);
}
