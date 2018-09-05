package com.cchao.simplelib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by Jason on 2016/3/18.
 */
public class PermissionUtils {

    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length > 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkSelfPermission(Activity activity, int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return shouldShowRequestPermission(activity, requestCode, permissions);
        }
        return true;
    }

    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermission(Activity activity, int requestCode, String... permissions) {
        if (!hasSelfPermissions(activity, permissions)) {
            activity.requestPermissions(permissions, requestCode);
            return false;
        }
        return true;
    }


}
