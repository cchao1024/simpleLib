package com.cchao.simplelib.util;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.core.Logs;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 对URL 操作
 * Created by cchao on 2017/7/6.
 */
public class UrlUtil {

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

    public static boolean isValidUrl(String str) {
        if (StringHelper.isEmpty(str)) {
            return false;
        }
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
            + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
            + "|" // 允许IP和DOMAIN（域名）
            + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
            + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
            + "[a-z]{2,6})" // first level domain- .com or .museum
            + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
            + "((/?)|" // a slash isn't required if there is no file name
            + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return str.matches(regex);
    }

    //region 从 URI 获取值或键值对

    /**
     * 获取url中的指定的key对应的值
     *
     * @param key 指定的key
     * @return key对应的值
     */
    public static String getValueFromUrl(String url, String key) {
        return getParams(url).get(key);
    }

    public static Map<String, String> getParams(String url) {
        String regEx = "(\\?|&+)(.+?)=([^&#]*)";//匹配参数名和参数值的正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        // LinkedHashMap是有序的Map集合，遍历时会按照加入的顺序遍历输出
        Map<String, String> paramMap = new LinkedHashMap<>();
        Logs.logEvent("[蚁丛旅游]"+p.toString());
        while (m.find()) {
            String paramName = m.group(2);//获取参数名
            String paramVal = m.group(3);//获取参数值
            paramMap.put(paramName, paramVal);
            Logs.logEvent("[蚁丛旅游]"+paramName + " " + paramVal);
        }

        return paramMap;
    }

    /**
     * 获取url中的指定的key对应的集合
     *
     * @param key 指定的key
     * @return key对应的集合
     */
    public static List<String> getValuesFromUrl(String url, String key) {
        List<String> valueList = Uri.parse(url).getQueryParameters(key);
        return valueList;
    }

    /**
     * 读取uri当中的参数集合
     */
    public static Map<String, String> getUriParams(String url) {
        return getUriParams(Uri.parse(url));
    }

    /**
     * 取URL的请求参数
     */
    public static Map<String, String> getParaMap(String url) {
        return getUriParams(Uri.parse(url));
    }

    /**
     * 读取uri当中的参数集合
     *
     * @param uri
     * @return
     */
    public static Map<String, String> getUriParams(Uri uri) {
        HashMap<String, String> ps = new HashMap<>();
        if (uri == null) {
            return ps;
        }
        try {
            Set<String> names = uri.getQueryParameterNames();
            for (String name : names) {
                String val = uri.getQueryParameter(name);
                ps.put(name, val);
            }
            return ps;
        } catch (Throwable throwable) {
            Logs.logException(uri.toString());
            return ps;
        }
    }
    //endregion

    //region 参数追加，构建URI

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
     * 对地址追加参数,如 "utm_campaign=share&test=1"
     *
     * @param uri
     * @param appendQuery
     */
    public static String appendParameter(String uri, String appendQuery) {
        try {
            URI oldUri = new URI(uri);

            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }

            URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());

            return newUri.toString();
        } catch (Throwable e) {
            Logs.logException(e);
            return uri;
        }
    }

    /**
     * 追加参数值,有重复的使用替换
     */
    public static String appendParameter(String url, String key, String value) {
        Map<String, String> ps = getUriParamsExtra(url);
        ps.put(key, value);
        try {
            Uri uri = Uri.parse(url);
            final Uri.Builder newUri = uri.buildUpon().clearQuery();
            Set<String> keys = ps.keySet();
            for (String k : keys) {
                newUri.appendQueryParameter(k, ps.get(k));
            }

            return newUri.build().toString();
        } catch (Throwable e) {
            Logs.logException(e);
            return url;
        }
    }

    /**
     * 追加参数值,有重复的使用替换
     */
    public static String appendParameter(String url, HashMap<String, String> hashMap) {
        Map<String, String> ps = getUriParamsExtra(url);

        Set<Map.Entry<String, String>> entrySet = hashMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            ps.put(entry.getKey(), entry.getValue());
        }

        try {
            Uri uri = Uri.parse(url);
            final Uri.Builder newUri = uri.buildUpon().clearQuery();
            Set<String> keys = ps.keySet();
            for (String k : keys) {
                newUri.appendQueryParameter(k, ps.get(k));
            }

            return newUri.build().toString();
        } catch (Throwable e) {
            Logs.logException(e);
            return url;
        }
    }
    //endregion

    /**
     * 解析出url地址的参数,纯字符解析,用系统的URI来解析对于有$号开头的参数是解析不了的
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> getUriParamsExtra(String urlStr) {
        try {
            if (StringHelper.isEmpty(urlStr)) {
                return new LinkedHashMap<String, String>();
            }

            // 没路径的补足路径
            if (!urlStr.startsWith("http")) {
                urlStr = "http://www.tmp.com/" + urlStr;
            }

            URL url = new URL(urlStr);
            Map<String, String> query_pairs = new LinkedHashMap<String, String>();
            String query = url.getQuery();
            if (query == null) {
                return new LinkedHashMap<String, String>();
            }
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx < 0)
                    continue;

                query_pairs.put(
                    URLDecoder.decode(pair.substring(0, idx), Const.STRING_UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), Const.STRING_UTF_8)
                );
            }
            return query_pairs;
        } catch (Throwable e) {
            Logs.logException(e);
            return new LinkedHashMap<String, String>();
        }
    }

    /**
     * 获取url中的指定的key对应的值
     *
     * @param url 指定的url对象
     * @param key 指定的key
     * @return key对应的值
     */
    public static String getValueFromUrlUnDecode(String url, String key) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return getValueFromUrlUnDecode(Uri.parse(url), key);
    }

    /**
     * 获取url中的指定的key对应的值
     *
     * @param uri 指定的Uri对象
     * @param key 指定的key
     * @return key对应的值
     */
    public static String getValueFromUrlUnDecode(Uri uri, String key) {
        if (uri.isOpaque()) {
            return null;
        }
        if (key == null) {
            return null;
        }

        final String query = uri.getEncodedQuery();
        if (query == null) {
            return null;
        }

        final String encodedKey = Uri.encode(key, null);
        final int length = query.length();
        int start = 0;
        do {
            int nextAmpersand = query.indexOf('&', start);
            int end = nextAmpersand != -1 ? nextAmpersand : length;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            if (separator - start == encodedKey.length()
                && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                if (separator == end) {
                    return "";
                } else {
                    return query.substring(separator + 1, end);
                }
            }

            // Move start to end of name.
            if (nextAmpersand != -1) {
                start = nextAmpersand + 1;
            } else {
                break;
            }
        } while (true);
        return null;
    }

    /**
     * 编码网址,这个方法会对整个网址进行编码,适合要将整个网址作为参数传递时
     * 比如往数据魔方传递数据
     *
     * @param url
     * @return
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, Const.STRING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
