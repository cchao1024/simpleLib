package com.cchao.simplelib.core;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.util.StringHelper;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;


/**
 * 系统参数 帮助类
 *
 * @author cchao
 * @version 2019/4/9.
 */
public class AndroidHelper {

    public static Context mContext = LibCore.getContext();

    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String mUUid = null;

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceID() {
        if (mUUid != null) {
            return mUUid;
        }
        mUUid = trimToEmpty(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        if (StringHelper.isEmpty(mUUid)) {
            // code from https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
            SharedPreferences sharedPrefs = mContext.getSharedPreferences(PREF_UNIQUE_ID, mContext.MODE_PRIVATE);
            mUUid = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (mUUid == null) {
                mUUid = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, mUUid);
                editor.apply();
            }
        }
        return mUUid;
    }

    public static String getDeviceNum() {
        return Settings.System.getString(LibCore.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 是否开启某个权限
     *
     * @param permissions 可变长类型
     * @return
     */
    public static boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 应用的版本号
     *
     * @return 2.3.0
     */
    public static String getVersionName() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            Logs.logException(e);
        }
        return "";
    }

    /**
     * 获取应用的版本号 (内部识别号)
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            Logs.logException(e);
        }
        return 0;
    }

    public static boolean versionThanLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean versionThanM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 检查activity 是不是已经被 destroy 了
     */
    public static boolean isContextDestroyed(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed()) {
                return true;
            } else if (activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取剪切板内容
     */
    public static String getClipboardShareData() {
        String data = "";
        try {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                if (item != null && item.getText() != null) {
                    data = item.getText().toString();
                }
            }
        } catch (Exception e) {
            Logs.logException(e);
        }
        return data;
    }

    /**
     * 清空剪切板内容
     */
    public static void clearClipboardData() {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("", "");
        clipboard.setPrimaryClip(data);
    }

    /**
     * 清空剪切板内容
     */
    public static void setClipboardData(String content,Runnable runnable) {
        try {
            //获取剪贴板管理器
            if (StringHelper.isEmpty(content)) {
                return;
            }
            ClipboardManager cm = (ClipboardManager) LibCore.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", content);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            runnable.run();
        } catch (Exception e) {
            Logs.logException(e);
        }
    }

    /**
     * 获取当前网络状态
     */
    public static String getNetworkType(Context context) {
        NetworkInfo networkInfo = null;
        if (hasPermissions(android.Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
        }

        if (networkInfo == null) {
            return NET_WORK_TYPE.NETWORK_TYPE_UNKNOWN;
        }

        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return NET_WORK_TYPE.NETWORK_TYPE_WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                switch (networkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NET_WORK_TYPE.NETWORK_TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NET_WORK_TYPE.NETWORK_TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NET_WORK_TYPE.NETWORK_TYPE_4G;
                }
                break;
        }
        return NET_WORK_TYPE.NETWORK_TYPE_UNKNOWN;
    }

    /**
     * 网络类型
     */
    interface NET_WORK_TYPE {
        String NETWORK_TYPE_WIFI = "WIFI";
        String NETWORK_TYPE_2G = "2G";
        String NETWORK_TYPE_3G = "3G";
        String NETWORK_TYPE_4G = "4G";
        String NETWORK_TYPE_NONE = "NONE";
        String NETWORK_TYPE_UNKNOWN = "UNKNOWN";
    }

    public static void restartApp() {
        Context context = LibCore.getContext();
        RxHelper.timerConsumer(300, aLong -> System.exit(0));
        Intent intent = context.getApplicationContext().getPackageManager()
            .getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1500, restartIntent);
    }
}
