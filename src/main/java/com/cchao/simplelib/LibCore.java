package com.cchao.simplelib;

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
        mContext = appContext;
        mInfoSupport = infoSupport;

        PrefHelper.init(mContext, mInfoSupport.getAppName());
    }


    public static Context getContext() {
        return mContext;
    }

    public static InfoSupport getInfo() {
        return mInfoSupport;
    }

    public static void setLibConfig(LibConfig mLibConfig) {
        LibCore.mLibConfig = mLibConfig;
    }

    public static LibConfig getLibConfig() {
        if (mLibConfig == null) {
            mLibConfig = new LibConfig()
                .setTitleBarStyle(Const.TitleStyle.title)
                .setOverrideCookieJar(false);
        }
        return mLibConfig;
    }

    public interface InfoSupport {

        /**
         * 获取 OkHttp 对象
         * @return 不复写就返回默认的实现
         */
        default OkHttpClient getOkHttpClient() {
            return OkHttpHelper.getDefault();
        }

        boolean isDebug();

        String getAppName();

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
    }

    /**
     * 上报给日志收集平台，通过 {@link com.cchao.simplelib.core.Logs} 调起
     */
    public interface ILogEvents {
        void logEvent(String tag, String event);

        void logException(Throwable e);
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

        public DefaultTitleBarDelegate getTitleBarDelegate(Context context, ViewGroup parent) {
            return new DefaultTitleBarDelegate(context, parent);
        }
    }
}