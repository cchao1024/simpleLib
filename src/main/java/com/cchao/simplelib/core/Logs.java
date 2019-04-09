package com.cchao.simplelib.core;

import android.util.Log;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.util.FileUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Log输出
 * 仅在 debug 模式下输出和保存日志
 */
public class Logs {
    public static final String DEFAULT_TAG = " [" + LibCore.getInfo().getAppName() + "] ";

    public static void d(String tag, String msg) {
        if (LibCore.getInfo().isDebug()) {
            Log.d(wrapTag(tag), msg);
        }
    }

    public static void d(String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(msg);
            Log.d(DEFAULT_TAG, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LibCore.getInfo().isDebug()) {
            Log.d(wrapTag(tag), msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(tag + ":" + msg);
            Log.e(wrapTag(tag), msg);
        }
    }

    public static void e(Throwable throwable) {
        Logs.e(wrapTag("App is crashed"), Log.getStackTraceString(throwable));
    }

    public static void e(String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(msg);
            Log.e(DEFAULT_TAG, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LibCore.getInfo().isDebug()) {
            Log.e(wrapTag(tag), msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(tag + ":" + msg);
            Log.i(wrapTag(tag), msg);
        }
    }

    public static void i(String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(msg);
            Log.i(DEFAULT_TAG, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LibCore.getInfo().isDebug()) {
            Log.i(wrapTag(tag), msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(tag + ":" + msg);
            Log.v(wrapTag(tag), msg);
        }
    }

    public static void v(String msg) {
        if (LibCore.getInfo().isDebug()) {
            FileUtils.writeLogText(msg);
            Log.v(DEFAULT_TAG, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LibCore.getInfo().isDebug()) {
            Log.v(wrapTag(tag), msg + "\n", tr);
        }
    }

    public static void logException(String e) {
        logException(new Throwable(e));
    }

    public static void logException(Throwable e) {
        Logs.e(e.toString());
        LibCore.getInfo().getLogEvents().logException(e);
        throwException(e);
    }

    private static void throwException(Throwable e) {
        if (!LibCore.getInfo().isDebug()) {
            return;
        }
        if (e instanceof SocketTimeoutException) {
            return;
        }
        if (e instanceof IOException) {
            return;
        }
        if (e instanceof TimeoutException) {
            return;
        }
        Logs.e("奔溃了", Log.getStackTraceString(e));
    }

    public static void logEvent(String event) {
        logEvent(Logs.DEFAULT_TAG, event);
    }

    public static void logEvent(String tag, String msg) {
        Logs.d(tag, msg);
        LibCore.getInfo().getLogEvents().logEvent(tag, msg);
    }

    /**
     * 格式化下 tag
     */
    private static String wrapTag(String tag) {
        return " [" + tag + "] ";
    }
}
