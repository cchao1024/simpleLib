package com.cchao.simplelib.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 对URL 操作
 * Created by cchao on 2017/7/6.
 */

public class UrlUtil {
    /**
     * 取URL的请求参数
     */
    public static Map<String, String> getParaMap(String url) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(url)) {// 如果URL不是空字符串
            url = url.substring(url.indexOf('?') + 1);
            String[] paramaters = url.split("&");
            for (String param : paramaters) {
                String[] values = param.split("=");
                map.put(values[0], values.length > 1 ? values[1] : "");
            }
        }
        return map;
    }

    /**
     * 将map转换成url domain 为空就只有 &key1=v1 & key2=v2 ...
     *
     * @param domain 域名，可为空
     */
    public static String joinUrlByMap(@Nullable String domain, Map<String, String> map) {
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
        //移除末尾&
        if (s.endsWith("&")) {
            s = StringHelper.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * 追加请求参数
     */
    public static String appendParament(@Nullable String url, String key, String value) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return url;
        }
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return buildUrl(url, map);
    }

    /**
     * 拼接请求
     *
     * @param requestUrl url
     * @param paramsMap  请求参数
     */
    public static String buildUrl(String requestUrl, Map<String, String> paramsMap) {
        Uri uri = Uri.parse(requestUrl);
        Uri.Builder builder = uri.buildUpon();
        if (paramsMap != null && paramsMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.appendQueryParameter(key, value);
            }
        }
        return builder.toString();
    }

    /**
     * 获取url中的指定的key对应的值
     *
     * @param url 指定的url对象
     * @param key 指定的key
     * @return key对应的值
     */
    public static String getValueFromUrl(String url, String key) {
        String value = Uri.parse(url).getQueryParameter(key);
        return value;
    }


    /**
     * 获取url中的指定的key对应的集合
     *
     * @param url 指定的url对象
     * @param key 指定的key
     * @return key对应的集合
     */
    public static List<String> getValuesFromUrl(String url, String key) {
        List<String> valueList = Uri.parse(url).getQueryParameters(key);
        return valueList;
    }
}
