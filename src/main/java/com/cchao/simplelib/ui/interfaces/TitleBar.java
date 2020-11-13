package com.cchao.simplelib.ui.interfaces;

import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.view.View;

import com.cchao.simplelib.core.UiHelper;

/**
 * 对 titleBar（标题栏） 的基本操作
 *
 * @author cchao
 * @version 2019/4/10.
 */
public interface TitleBar {

    /**
     * 设置标题
     *
     * @param text string
     */
    void setTitleText(String text);

    default void setTitleText(@StringRes int id) {
        setTitleText(UiHelper.getString(id));
    }

    /**
     * 显示整个titleBar 默认显示
     *
     * @param visible true-显示  false-隐藏
     */
    void setTitleBarVisible(boolean visible);

    /**
     * 显示左边后退按钮 默认显示
     *
     * @param visible true-显示  false-隐藏
     */
    void setBackActionVisible(boolean visible, View.OnClickListener listener);

    /**
     * 添加 右侧菜单图标和点击
     *
     * @param icon 图标
     * @return 返回这个view
     */
    View addTitleMenuItem(Drawable icon, View.OnClickListener listener);

    default View addTitleMenuItem(@DrawableRes int icon, View.OnClickListener listener) {
        return addTitleMenuItem(UiHelper.getDrawable(icon), listener);
    }

    /**
     * 添加 右侧菜单view和点击
     *
     * @param menuView 菜单View
     * @return 返回这个view
     */
    void addTitleMenuItem(View menuView, View.OnClickListener listener);
}
