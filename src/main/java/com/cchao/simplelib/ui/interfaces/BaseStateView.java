package com.cchao.simplelib.ui.interfaces;

import androidx.annotation.StringDef;
import android.view.ViewGroup;

import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description: 各种状态操作， {@link BaseTitleBarActivity}
 * Created by cchao on 2017/3/16.
 */
public interface BaseStateView extends BaseView {

    String LOADING = "Loading";
    String NET_ERROR = "NetError";
    String CONTENT = "Content";
    String EMPTY = "Empty";

    @StringDef({LOADING, NET_ERROR, CONTENT, EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }

    /** 切换不同的View状态 */
    void switchView(@ViewType String viewType);

    ViewGroup getRootViewGroup();
}
