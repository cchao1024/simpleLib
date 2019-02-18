package com.cchao.simplelib.core;

import android.app.Activity;
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
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cchao.simplelib.LibCore;

/**
 * 一些常用的 ui核心工具方法
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
        , Pair<String, DialogInterface.OnClickListener> confirm
        , Pair<String, DialogInterface.OnClickListener> cancel) {

        new AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(confirm.first, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (confirm.second == null) {
                        return;
                    }
                    confirm.second.onClick(dialogInterface, i);
                }
            })
            .setNegativeButton(cancel.first, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (cancel.second == null) {
                        return;
                    }
                    cancel.second.onClick(dialogInterface, i);
                }
            })
            .show();
    }
    //</editor-fold>


    /**
     * 隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }


    /**
     * 显示软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }
}
