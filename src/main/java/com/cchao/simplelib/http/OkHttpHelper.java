package com.cchao.simplelib.http;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.http.cookie.CookieJarImpl;
import com.cchao.simplelib.http.cookie.store.PersistentCookieStore;
import com.cchao.simplelib.http.intercaptor.RequestLogInterceptor;
import com.cchao.simplelib.http.intercaptor.RespExceptionLogInterceptor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private static OkHttpClient mHttpClient = LibCore.getInfo().getOkHttpClient();
    private static RequestLogInterceptor mReqLogInterceptor;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient getDefault() {
        //初始化OkHttpClient
        return new OkHttpClient().newBuilder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();
    }

    /**
     * 获取网络交互client，首次get时会对其添加数据统计的拦截器。
     */
    public static OkHttpClient getClient() {
        // 如果请求拦截为空，就追加拦截器
        if (mReqLogInterceptor == null) {
            // 添加日志收拦截器
            mHttpClient = mHttpClient.newBuilder()
                .addInterceptor(mReqLogInterceptor = new RequestLogInterceptor())
                .addInterceptor(new RespExceptionLogInterceptor())
                .build();

            // 应用层不传入自定义 cookieJar，则写入默认的
            if (!LibCore.getLibConfig().isOverrideCookieJar()) {
                mHttpClient = mHttpClient.newBuilder()
                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(LibCore.getContext())))
                    .build();
            }
        }
        return mHttpClient;
    }

    /**
     * 发送post 请求
     *
     * @param url      url
     * @param params   映射参数
     * @param callback 回调
     */
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

    /**
     * 发送post 请求
     *
     * @param url         url
     * @param requestBody requestBody
     * @param callback    回调
     */
    public static void post(String url, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();

        getClient().newCall(request).enqueue(callback);
    }

    /**
     * 发送 get 请求
     *
     * @param url      url
     * @param params   映射参数
     * @param callback 回调
     */
    public static void get(String url, Map<String, String> params, Callback callback) {

        OkHttpClient client = getClient();
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpBuilder = httpUrl.newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                httpBuilder.addQueryParameter(key, value);
            }
        }

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送 get 请求
     *
     * @param url      url
     * @param callback 回调
     */
    public static void get(String url, Callback callback) {
        get(url, null, callback);
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
