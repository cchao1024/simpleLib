package com.cchao.simplelib.util;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.UiHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    public static final String EMPTY = "";

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String upperFirstCase(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public static String formatSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    public static boolean isEmail(String email) {
        if (StringHelper.isEmpty(email)) {
            return false;
        }

        Pattern p = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    public static boolean isNotEmpty(Object s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Object s) {
        return s == null || s.toString().length() == 0;
    }

    public static boolean isEmpty(EditText editText) {
        if (editText == null) {
            return true;
        }
        String s = editText.getText().toString();
        return s.length() == 0;
    }

    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Space(String s) {
        return s == null ? "" : s;
    }

    public static Spanned forHtml(String source) {
        return Html.fromHtml(source, source1 -> {
            /*Drawable drawable = null;
            URL url;
            try {
                url = new URL(source1);
                drawable = Drawable.createFromStream(url.openStream(), System.currentTimeMillis() + ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;*/
            InputStream is = null;
            try {
                is = (InputStream) new URL(source1).getContent();
                Drawable d = Drawable.createFromStream(is, "src");
                int width = UiHelper.getScreenWidth() - UiHelper.dp2px(36);
                int height = (int) (width / (d.getIntrinsicWidth() * 1.0f / d.getIntrinsicHeight()));
                d.setBounds(0, 0, width, height);
                is.close();
                return d;
            } catch (Exception e) {
                Logs.logException(e);
                return null;
            }
        }, null);
    }
}
