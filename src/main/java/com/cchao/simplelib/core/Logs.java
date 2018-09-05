package com.cchao.simplelib.core;

import android.util.Log;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.util.FileUtils;

/**
 * Log输出
 *
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

    /**
     * 格式化下 tag
     */
    private static String wrapTag(String tag) {
        return " [" + tag + "] ";
    }
}
