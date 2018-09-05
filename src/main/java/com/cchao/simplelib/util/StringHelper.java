package com.cchao.simplelib.util;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.Html;
import android.text.TextUtils;

import com.cchao.simplelib.core.Logs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
    public static final int INDEX_NOT_FOUND = -1;

    public static final String EMPTY = "";
    static final String CHARSET = "utf-8";

    static final Pattern CRLF = Pattern.compile("(/n|//n)");

    public static boolean isValidHtml(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        Pattern p = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static ArrayMap<String, String> urlRequest(String url) {
        ArrayMap<String, String> mapRequest = new ArrayMap<String, String>();

        try {
            String[] arrSplit = null;

            String strUrlParam = truncateUrlPage(url);
            if (strUrlParam == null) {
                return mapRequest;
            }
            arrSplit = strUrlParam.split("[&]");
            for (String strSplit : arrSplit) {
                String[] arrSplitEqual = null;
                arrSplitEqual = strSplit.split("[=]");

                if (arrSplitEqual.length > 1) {
                    mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
                } else {
                    if (!arrSplitEqual[0].equals("")) {
                        mapRequest.put(arrSplitEqual[0], "");
                    }
                }
            }
        } catch (Throwable e) {
            ExceptionCollect.logException(e);
        }
        return mapRequest;
    }

    /**
     * 将map转换成url domain 为空就只有 &key1=v1 & key2=v2 ...
     *
     * @param domain 域名，可为空
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(@Nullable String domain, Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(domain)) {
            sb.append(domain).append("?");
        } else {
            sb.append("&");
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey())
                .append("=")
                .append(entry.getValue())
                .append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringHelper.substringBeforeLast(s, "&");
        }
        return s;
    }

    public static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Logs.e(e);

            return url;
        }
    }

    public static String filter(String response) {
        Matcher m = CRLF.matcher(response);
        return m.replaceAll("");
    }

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
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        Pattern p = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"); // 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 是否包含特殊符号
     *
     * @param string content
     * @return
     */
    public static boolean isIncludeSpecialChar(String string) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    public static String trimToEmpty(final String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String substringBefore(final String str, final String separator) {
        if (TextUtils.isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
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
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfter(final String str, final String separator) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBeforeLast(final String str, final String separator) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String join(Object[] arr, char separator) {
        if (arr == null) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder(arr.length << 4);
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                stringBuilder.append(separator);
            }
            stringBuilder.append(arr[i]);
        }
        return stringBuilder.toString();
    }
}
