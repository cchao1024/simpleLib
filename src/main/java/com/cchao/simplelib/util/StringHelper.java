package com.cchao.simplelib.util;

import android.text.TextUtils;

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

    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * <p>Gets the substring after the first occurrence of a separator.
     * The separator is not returned.</p>
     * <p>
     * <p>A {@code null} string input will return {@code null}.
     * An empty ("") string input will return the empty string.
     * A {@code null} separator will return the empty string if the
     * input string is not {@code null}.</p>
     * <p>
     * <p>If nothing is found, the empty string is returned.</p>
     * <p>
     * <pre>
     * StringHelper.substringAfter(null, *)      = null
     * StringHelper.substringAfter("", *)        = ""
     * StringHelper.substringAfter(*, null)      = ""
     * StringHelper.substringAfter("abc", "a")   = "bc"
     * StringHelper.substringAfter("abcba", "b") = "cba"
     * StringHelper.substringAfter("abc", "c")   = ""
     * StringHelper.substringAfter("abc", "d")   = ""
     * StringHelper.substringAfter("abc", "")    = "abc"
     * </pre>
=======
     * null转为长度为0的字符串
>>>>>>> Stashed changes
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Space(String s) {
        return s == null ? "" : s;
    }
}
