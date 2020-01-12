package com.cchao.simplelib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 常用跳转
 *
 * @author cchao
 * @since 2019-12-25
 */
public class IntentHelper {

    /**
     * 打开拨号盘
     */
    public static void tel(String mobile, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mobile);
        intent.setData(data);
        context.startActivity(intent);
    }
}
