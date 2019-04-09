package com.cchao.simplelib.http;

import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求的 日志收集拦截器
 *
 * @author cchao
 * @version 2019/4/9.
 */
public class RequestLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String originUrl = request.url().url().toString();

            //收集请求体
            String postBody = "";
            if (request.body() instanceof FormBody) {
                FormBody formBody = ((FormBody) request.body());
                for (int i = 0; i < formBody.size(); i++) {
                    postBody += formBody.encodedName(i) + " = " + formBody.encodedValue(i) + " & ";
                }
            }
            if (StringHelper.isNotEmpty(postBody)) {
                postBody = "\n请求body：" + postBody;
            }
            Logs.logEvent("发送请求：", "Url : 【" + originUrl + '】' + postBody);

        } catch (Exception e) {
            ExceptionCollect.logException(e);
        }
        return chain.proceed(request);
    }
}
