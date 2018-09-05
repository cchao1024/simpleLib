package com.cchao.simplelib.util;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UIUtils {

    private static long lastClickTime;
    /**
     * 第一个可见的item的位置
     */
    private static int[] firstScrollPositions;

    public static void updateDrawableColor(View view, int color) {
        if (view == null) {
            return;
        }

        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        }
    }

    public static void updateDrawableStroke(View view, int color) {
        if (view == null) {
            return;
        }

        Drawable background = view.getBackground();
        if (background instanceof GradientDrawable) {
            int width = view.getResources().getDimensionPixelSize(R.dimen.dp_1);

            ((GradientDrawable) background).setStroke(width, color);
        }
    }

    public static void setUnderline(TextView textview) {
        textview.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textview.getPaint().setAntiAlias(true); //抗锯齿
    }

    public static void navigateUpOrBack(Activity currentActivity, Class<? extends Activity> syntheticParentActivity) {
        // Retrieve parent activity from AndroidManifest.
        Intent intent = NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if (intent == null && syntheticParentActivity != null) {
            try {
                intent = NavUtils.getParentActivityIntent(currentActivity, syntheticParentActivity);
            } catch (PackageManager.NameNotFoundException e) {
                Logs.e(e);
            }
        }

        if (intent == null) {
            // No parent defined in manifest. This indicates the activity may be used by
            // in multiple flows throughout the app and doesn't have a strict parent. In
            // this case the navigation up button should act in the same manner as the
            // back button. This will result in users being forwarded back to other
            // applications if currentActivity was invoked from another application.
            currentActivity.onBackPressed();
        } else {
            if (NavUtils.shouldUpRecreateTask(currentActivity, intent)) {
                // Need to synthesize a backstack since currentActivity was probably invoked by a
                // different app. The preserves the "Up" functionality within the app according to
                // the activity hierarchy defined in AndroidManifest.xml via parentActivity
                // attributes.
                TaskStackBuilder builder = TaskStackBuilder.create(currentActivity);
                builder.addNextIntentWithParentStack(intent);
                builder.startActivities();
            } else {
                // Navigate normally to the manifest defined "Up" activity.
                NavUtils.navigateUpTo(currentActivity, intent);
            }
        }
    }

    public static void setColorSchemeResources(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @ColorInt
    public static int shiftColorDown(@ColorInt int color) {
        return shiftColor(color, 0.9f);
    }

    @ColorInt
    public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0f, to = 2.0f) float by) {
        if (by == 1f) {
            return color;
        }
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= by; // value component
        return Color.HSVToColor(hsv);
    }

    public static int getDrawerWidth(Resources res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            if (res.getConfiguration().smallestScreenWidthDp >= 600 || res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // device is a tablet
                return (int) (320 * res.getDisplayMetrics().density);
            } else {
                return (int) (res.getDisplayMetrics().widthPixels - (70 * res.getDisplayMetrics().density));
            }
        } else { // for devices without smallestScreenWidthDp reference calculate if device screen is over 600 dp
            if ((res.getDisplayMetrics().widthPixels / res.getDisplayMetrics().density) >= 600 || res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return (int) (320 * res.getDisplayMetrics().density);
            } else {
                return (int) (res.getDisplayMetrics().widthPixels - (70 * res.getDisplayMetrics().density));
            }
        }
    }

    public static int getRightDrawerWidth(Resources res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            if (res.getConfiguration().smallestScreenWidthDp >= 600 || res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // device is a tablet
                return (int) (320 * res.getDisplayMetrics().density);
            } else {
                return (int) (res.getDisplayMetrics().widthPixels - (80 * res.getDisplayMetrics().density));
            }
        } else { // for devices without smallestScreenWidthDp reference calculate if device screen is over 600 dp
            if ((res.getDisplayMetrics().widthPixels / res.getDisplayMetrics().density) >= 600 || res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return (int) (320 * res.getDisplayMetrics().density);
            } else {
                return (int) (res.getDisplayMetrics().widthPixels - (80 * res.getDisplayMetrics().density));
            }
        }
    }

    public static boolean isMoreThanAPI19() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
    }

    public static boolean isMoreThanAPI21() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static void bringToFront(View source, View target, int value) {
        setZ(source, target, value);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setZ(View source, View target, int value) {
        float z = target.getZ() + value;

        source.setZ(z);
    }

    public static void changeLayoutParams(View view, int height, int width) {
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;

        view.setLayoutParams(layoutParams);
    }

    public static void setActionBar(Activity activity, String title) {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(title);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    // @see https://github.com/android/platform_frameworks_base/blob/master/core/java/android/os/AsyncTask.java
    // 这个暂时都主要给执行sqlite的后台线程使用保证其都是按先后顺序执行的
    public static void execInBackgroundInSerial(Runnable r) {
        final Runnable _r = r;
        new AsyncTask() {
            @Override
            protected synchronized Object doInBackground(Object... params) {
                _r.run();

                return null;
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void execInBackgroundInPool(Runnable r) {
        final Runnable _r = r;
        new AsyncTask() {
            @Override
            protected synchronized Object doInBackground(Object... params) {
                _r.run();

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static ExecutorService datacubeThread = Executors.newFixedThreadPool(1);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void execInBackgroundInDatacubeSerial(Runnable r) {
        final Runnable _r = r;
        new AsyncTask() {
            @Override
            protected synchronized Object doInBackground(Object... params) {
                _r.run();
                // 强制执行完要停三秒才操作下一个
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    ExceptionCollect.logException(e);
                }
                return null;
            }
        }.executeOnExecutor(datacubeThread);
    }

    @SuppressWarnings("unchecked")
    public static <T> T findViewById(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static void setStatusBarTranslucent(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(0x44000000);
        }
    }


    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
            .getDisplayMetrics());
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return dp2px(LibCore.getContext(), dpVal);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources()
            .getDisplayMetrics());
    }


    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static boolean isNotShake(int timeInMillis) {
        //防止用户多次点击
        long currentTime = Calendar.getInstance().getTimeInMillis();

        if (currentTime - lastClickTime < timeInMillis) {
            return false;
        }
        lastClickTime = currentTime;
        return true;
    }

    public static enum LayoutManagerType {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }


    private static int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
