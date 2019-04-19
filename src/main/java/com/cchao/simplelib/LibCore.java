package com.cchao.simplelib;

import android.content.Context;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.http.OkHttpHelper;

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

        OkHttpHelper.init(mInfoSupport.getOkHttpClient());
    }

    public static Context getContext() {
        return mContext;
    }

    public static InfoSupport getInfo() {
        return mInfoSupport;
    }

    public interface InfoSupport {

        OkHttpClient getOkHttpClient();

        boolean isDebug();

        String getAppName();

        /**
         * 复写自定义的 Lib 的配置项
         *
         * @return 配置类，不复写会返回默认的
         */
        default LibConfig getLibConfig() {
            return new LibConfig()
                .setTitleBarStyle(Const.TitleStyle.title)
                .setOverrideCookieJar(false);
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
    }
}