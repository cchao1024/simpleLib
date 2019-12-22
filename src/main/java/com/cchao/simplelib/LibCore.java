package com.cchao.simplelib;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.http.OkHttpHelper;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.BaseView;
import com.cchao.simplelib.ui.interfaces.impl.DefaultBaseViewDelegate;
import com.cchao.simplelib.ui.interfaces.impl.DefaultStateViewDelegate;
import com.cchao.simplelib.ui.interfaces.impl.DefaultTitleBarDelegate;
import com.cchao.simplelib.util.LanguageUtil;
import com.cchao.simplelib.view.state.StateSwitchable;
import com.cchao.simplelib.view.state.field.FieldStateLayout;
import com.didichuxing.doraemonkit.DoraemonKit;

import okhttp3.OkHttpClient;

/**
 * simpleLib 的核心，对各helper类进行初始化
 *
 * @author cchao
 * @version 18-5-13.
 */
public class LibCore {
    private static Context mContext;
    private static InfoSupport mInfoSupport;
    private static LibConfig mLibConfig;

    /**
     * 初始化 lib
     *
     * @param appContext  应用上下文
     * @param infoSupport 应用信息提供
     */
    public static void init(Context appContext, InfoSupport infoSupport) {
        LibConfig defaultConfig = new LibConfig()
            .setTitleBarStyle(Const.TitleStyle.title)
            .setOverrideCookieJar(false);

        init(appContext,infoSupport,defaultConfig);
    }

    /**
     * 初始化 lib
     *
     * @param appContext  应用上下文
     * @param infoSupport 应用信息提供
     * @param libConfig 其他配置项
     */
    public static void init(Context appContext, InfoSupport infoSupport, LibConfig libConfig) {
        mContext = appContext;
        mInfoSupport = infoSupport;
        LibCore.mLibConfig = libConfig;

        PrefHelper.init(mContext, mInfoSupport.getAppName());
        initDebugMode();
        LanguageUtil.init();
    }

    /**
     * debug 环境下的类库初始化
     */
    private static void initDebugMode() {
        if (!mInfoSupport.isDebug()) {
            return;
        }
        // 滴滴 DoKit init
        if (LibCore.mLibConfig.mToggleDokit) {
            DoraemonKit.install((Application) mContext);
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isDebug() {
        return mInfoSupport.isDebug();
    }

    public static InfoSupport getInfo() {
        return mInfoSupport;
    }

    public static LibConfig getLibConfig() {
        return mLibConfig;
    }

    public interface InfoSupport {

        /**
         * 获取 OkHttp 对象
         *
         * @return 不复写就返回默认的实现
         */
        default OkHttpClient getOkHttpClient() {
            return OkHttpHelper.getDefault();
        }

        boolean isDebug();

        String getAppName();

        default int getAppVersionCode() {
            return 1;
        }

        /**
         * 上报给日志收集平台
         */
        default ILogEvents getLogEvents() {
            return new ILogEvents() {
                @Override
                public void logEvent(String tag, String event) {

                }

                @Override
                public void logException(Throwable e) {

                }
            };
        }

        /**
         * 返回 router配置项
         */
        default RouterConfig getRouterConfig() {
            return new RouterConfig() {
            };
        }
    }

    /**
     * 上报给日志收集平台，通过 {@link com.cchao.simplelib.core.Logs} 调起
     */
    public interface ILogEvents {
        void logEvent(String tag, String event);

        void logException(Throwable e);
    }

    /**
     * Router 的角色权限回调。，通过 {@link com.cchao.simplelib.core.Router} 做跳转权限处理
     * ,如 checkLogin标识是否需要检查登录状态
     */
    public interface RouterConfig {
        /**
         * 返回是否登录,
         * 返回true 可能会执行跳转目标页，
         * 返回false意味权限不够，由应用层决定是否跳转验证页面（如 登录页）
         *
         * @param isNeedLogin 如果需要登录，则需要复写跳转登录的代码
         */
        default boolean checkLogin(Context context, boolean isNeedLogin) {
            return true;
        }

        /**
         * 返回是否处于特定状态,
         * 返回true 执行跳转目标页
         * 返回false 意味权限不够，由应用层决定是否跳转验证页面
         *
         * @param isNeedObtainStatus 是否需要跳转获取权限页，则需要复写跳转代码
         */
        default boolean checkStatus(Context context, String statusName, boolean isNeedObtainStatus) {
            return true;
        }

    }

    /**
     * 可选的配置类
     */
    public static class LibConfig {
        /**
         * TitleBar 样式管理
         * 1. 普通线性 title
         * 2. toolbar实现
         */
        Const.TitleStyle mTitleBarStyle = Const.TitleStyle.title;

        /**
         * 是否应用层覆盖Lib默认cookie的管理
         */
        boolean mOverrideCookieJar = false;
        boolean mToggleDokit = false;

        public Const.TitleStyle getTitleBarStyle() {
            return mTitleBarStyle;
        }

        public LibConfig setTitleBarStyle(Const.TitleStyle titleBarStyle) {
            mTitleBarStyle = titleBarStyle;
            return this;
        }

        public boolean isOverrideCookieJar() {
            return mOverrideCookieJar;
        }

        public LibConfig setOverrideCookieJar(boolean overrideCookieJar) {
            mOverrideCookieJar = overrideCookieJar;
            return this;
        }

        public BaseView getBaseViewDelegate(Context context) {
            return new DefaultBaseViewDelegate(context);
        }

        public BaseStateView getStateViewDelegate(Context context, View childContent, Runnable retryCallBack) {
            return new DefaultStateViewDelegate(context, childContent, retryCallBack);
        }

        /**
         * 返回局部多状态加载View
         */
        public StateSwitchable getFieldStateView(Context context) {
            return new FieldStateLayout(context);
        }

        /*        */

        /**
         * 返回Recycler的多状态加载View
         *//*
        public StateSwitchable getRecyclerStateView(Context context) {
            return new FieldStateLayout(context);
        }*/
        public DefaultTitleBarDelegate getTitleBarDelegate(Context context, ViewGroup parent) {
            return new DefaultTitleBarDelegate(context, parent);
        }
    }
}