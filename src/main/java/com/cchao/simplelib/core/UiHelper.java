package com.cchao.simplelib.core;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.widget.Toast;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.util.Tuple;

/**
 * 一些常用的ui核心工具方法
 *
 * @author cchao
 * @version 18-5-13.
 */
public class UiHelper {

    //<editor-fold desc="对 px的转换">

    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue
            , LibCore.getContext().getResources().getDisplayMetrics());
    }

    public static float sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue
            , LibCore.getContext().getResources().getDisplayMetrics());
    }

    //</editor-fold>

    //<editor-fold desc="toast 操作">

    static Toast mToast;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean mIsOverwrite;

    public static void setOverwriteToast(boolean bol) {
        mIsOverwrite = bol;
    }

    public static void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showLongToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    private static void showToast(@StringRes int resId, int duration) {
        showToast(LibCore.getContext().getResources().getString(resId), duration);
    }

    private static void showToast(CharSequence text, int duration) {
        if (mIsOverwrite) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
        }
        mToast = Toast.makeText(LibCore.getContext(), text, duration);
        mToast.setText(text);
        mToast.setDuration(duration);
        Logs.d("toast " + text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.show();
            }
        });
    }

    //</editor-fold>

    public static void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    //<editor-fold desc="Compat 方法">

    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(LibCore.getContext(), id);
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(LibCore.getContext(), id);
    }

    public static String getString(int id) {
        return LibCore.getContext().getString(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return LibCore.getContext().getResources().getStringArray(id);
    }

    //</editor-fold>


    //<editor-fold desc="Dialog 操作">

    public static void showItemsDialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listener != null) {
                        listener.onClick(dialogInterface, i);
                    }
                }
            }).show();
    }

    public static void showConfirmDialog(Context context, String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listener == null) {
                        return;
                    }
                    listener.onClick(dialogInterface, i);
                }
            }).show();
    }

    public static void showCancelDialog(Context context, String msg
        , Tuple.Tuple2<String, DialogInterface.OnClickListener> confirm
        , Tuple.Tuple2<String, DialogInterface.OnClickListener> cancel) {

        new AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(confirm.a, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (confirm.b == null) {
                        return;
                    }
                    confirm.b.onClick(dialogInterface, i);
                }
            })
            .setNegativeButton(cancel.a, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (cancel.b == null) {
                        return;
                    }
                    cancel.b.onClick(dialogInterface, i);
                }
            })
            .show();
    }
    //</editor-fold>
}
