package com.cchao.simplelib.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.cchao.simplelib.LibCore;

public class DeviceInfo {

    private static int mScreenWidth;
    private static int mScreenHeight;

    public static int getScreenWidth() {
        if (mScreenWidth == 0) {
            Resources resources = LibCore.getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }
        return mScreenWidth;
    }

    public static int getScreenHeight() {
        if (mScreenHeight == 0) {
            Resources resources = LibCore.getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }
        return mScreenHeight;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
            & Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    public static String getDeviceNum() {
        return Settings.System.getString(LibCore.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}