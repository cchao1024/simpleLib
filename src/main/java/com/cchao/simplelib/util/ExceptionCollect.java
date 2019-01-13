package com.cchao.simplelib.util;

import android.util.Log;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.Logs;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * description
 * author  cchao
 * date  2017/2/24
 **/
public class ExceptionCollect {

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

}
