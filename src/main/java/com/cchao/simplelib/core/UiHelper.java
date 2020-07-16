package com.cchao.simplelib.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.util.CallBacks;

/**
 * 一些常用的 ui核心工具方法
 *
 * @author cchao
 * @version 18-5-13.
 */
public class UiHelper {

    private static Context getContext() {
        return LibCore.getContext();
    }

    //<editor-fold desc="对 px的转换 屏幕宽高">

    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue
            , getContext().getResources().getDisplayMetrics());
    }

    public static float sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue
            , getContext().getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕的宽度px
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
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
        showToast(getContext().getResources().getString(resId), duration);
    }

    private static void showToast(CharSequence text, int duration) {
        if (mIsOverwrite) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
        }

        runOnUiThread(() -> {
            mToast = Toast.makeText(getContext(), text, duration);
            mToast.setText(text);
            mToast.setDuration(duration);
            Logs.d("toast " + text);
            mToast.show();
        });
    }

    //</editor-fold>

    //<editor-fold desc="对View的操作">

    /**
     * 设置View 显示 visible 或 gone
     *
     * @param view view
     * @param bool if true > visible
     *             if false > gone
     */
    public static void setVisibleElseGone(View view, boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置View 显示 visible 或 inVisible
     *
     * @param view view
     * @param bool if true > visible
     *             if false > gone
     */
    public static void setVisibleElseInVisible(View view, boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * view 是否是visible
     *
     * @param view
     * @return
     */
    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void updateLayoutParams(View view, int height, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;

        view.setLayoutParams(layoutParams);
    }

    public static void setDeleteLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textView.getPaint().setAntiAlias(true);
    }
    //</editor-fold>

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    //<editor-fold desc="获取资源 Compat 方法">

    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    public static String getString(int id) {
        return getContext().getString(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return getContext().getResources().getStringArray(id);
    }

    //</editor-fold>

    //<editor-fold desc="Dialog 操作">
    public static ProgressDialog showProgress(Context context, String msg) {
        if (AndroidHelper.isContextDestroyed(context)) {
            return null;
        }
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

    public static void dismissProgress(ProgressDialog dialog) {
        if (dialog == null || AndroidHelper.isContextDestroyed(dialog.getContext())) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void showItemsDialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items, (dialogInterface, i) -> {
                if (listener != null) {
                    listener.onClick(dialogInterface, i);
                }
            }).show();
    }

    public static void showConfirmDialog(Context context, String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(UiHelper.getString(R.string.confirm), (dialogInterface, i) -> {
                if (listener == null) {
                    return;
                }
                listener.onClick(dialogInterface, i);
            }).show();
    }

    public static void showCancelDialog(Context context, String msg
        , Pair<String, DialogInterface.OnClickListener> confirm
        , Pair<String, DialogInterface.OnClickListener> cancel) {

        new AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(confirm.first, (dialogInterface, i) -> {
                if (confirm.second == null) {
                    return;
                }
                confirm.second.onClick(dialogInterface, i);
            })
            .setNegativeButton(cancel.first, (dialogInterface, i) -> {
                if (cancel.second == null) {
                    return;
                }
                cancel.second.onClick(dialogInterface, i);
            })
            .show();
    }

    public static void showEditDialog(Context context, String title, String hint, CallBacks.Str callBack) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        EditText editText = viewGroup.findViewById(R.id.edit);
        editText.setHint(hint);
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setView(viewGroup)
            .setPositiveButton(UiHelper.getString(R.string.confirm), (dialogInterface, i) -> {
                callBack.onCallBack(editText.getText().toString());
            }).setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
            //
            dialogInterface.dismiss();
        }).show();
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

    /**
     * 状态栏透明
     */
    public static void transparentStatusBar(Window window) {
        if (AndroidHelper.versionThanM()) {
            window.setStatusBarColor(UiHelper.getColor(R.color.transparent));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 状态栏透明
     */
    public static void setStatusBarColor(Window window,int color) {
        if (AndroidHelper.versionThanM()) {
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
