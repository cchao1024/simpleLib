package com.cchao.simplelib.http;

import android.content.Context;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * okhttp 简单 get,post封装，提供 string和对象bean响应返回
 *
 * @author cchao
 * @version 18-5-13.
 */
public class OkHttpHelper {
    private static OkHttpClient mHttpClient = null;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Context mContext;

    public static void init(OkHttpClient okHttpClient) {
        mHttpClient = okHttpClient;
    }

    public static OkHttpClient getClient() {
        if (mHttpClient == null) {
            throw new NullPointerException("OkHttpClient is null, you must init it with LibCore.init()");
        }
        return mHttpClient;
    }

    public static void post(String url, Map<String, String> params, Callback callback) {
        OkHttpClient client = getClient();

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();

        client.newCall(request).enqueue(callback);
    }

    public static void get(String url, Map<String, String> params, Callback callback) {

        OkHttpClient client = getClient();
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpBuider = httpUrl.newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                httpBuider.addQueryParameter(key, value);
            }
        }

        Request request = new Request.Builder().url(httpBuider.build()).build();
        client.newCall(request).enqueue(callback);
    }

    public static String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        try {
            Response response = getClient().newCall(request).execute();
            ResponseBody resBody = response.body();
            if (resBody == null) {
                return null;
            }
            return resBody.string();
        } catch (IOException e) {
            return null;
        }
    }
}
