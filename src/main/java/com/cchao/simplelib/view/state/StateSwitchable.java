package com.cchao.simplelib.view.state;

import android.view.View;

/**
 * 状态切换行为
 *
 * @author cchao
 * @version 2019-07-03.
 */
public interface StateSwitchable {

    /**
     * 状态切换，依赖 MultiStateView 的状态
     *
     * @param state MultiStateView.VIEW_STATE_LOADING 等
     */
    void setViewState(int state);

    void setReloadListener(View.OnClickListener onClickListener);
}
