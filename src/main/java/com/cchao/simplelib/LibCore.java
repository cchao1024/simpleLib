package com.cchao.simplelib;

import android.content.Context;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.http.OkHttpHelper;

import okhttp3.OkHttpClient;

/**
 * simpleLib 的核心，对各helper类进行初始化
 * @author cchao
 * @version 18-5-13.
 */
public class LibCore {
    private static Context mContext;
    private static InfoSupport mInfoSupport;

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
}