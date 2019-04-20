package com.cchao.simplelib.http.intercaptor;

import android.text.TextUtils;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.core.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 异常响应收集, 收集比如 503，500 这类的错误
 *
 * @author cchao
 * @version 2019/4/9.
 */
public class RespExceptionLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        String url = request.url().url().toString();
        try {
            response = chain.proceed(request);

            // 正常响应
            if (response.isSuccessful()) {
                // 获取响应体buffer clone写入
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                // 复制一份响应
                String json = buffer.clone().readString(Const.CHARSET_UTF_8).trim();

                try {
                    if (TextUtils.isEmpty(json)) {
                        Logs.logException(RespException.of(Const.RESP_TYPE.JSON_EMPTY, "返回了空值", "url : " + url));
                    } else {
                        new JSONObject(json);
                    }
                } catch (JSONException e) {
                    Logs.logException(RespException.of(Const.RESP_TYPE.JSON_ERROR, "接口返回不能转成json格式",
                        "url : " + url + " json : " + json + " exception " + e.getMessage()));
                } catch (Exception e) {
                    Logs.logException(e);
                }
                return response;
            }
            // 异常响应
            switch (response.code()) {
                case 500:
                    Logs.logException(RespException.of(Const.RESP_TYPE.RESP_500, "响应了500", "url : " + url));
                    break;
                case 404:
                    Logs.logException(RespException.of(Const.RESP_TYPE.RESP_404, "响应了404", "url : " + url));
                    break;
                case 503:
                    Logs.logException(RespException.of(Const.RESP_TYPE.RESP_503, "响应了503", "url : " + url));
                    break;
                default:
                    Logs.logException(RespException.of(Const.RESP_TYPE.RESP_FAIL + response.code(), "出错的响应", "url : " + url));
                    break;
            }
        } catch (SocketTimeoutException e) {
            Logs.logException(RespException.of(Const.RESP_TYPE.API_TIME_OUT, "接口请求超时了", "url : " + url));
            throw e;
        }
        return response;
    }

    static class RespException extends RuntimeException {

        static final long serialVersionUID = -70348971907439L;

        public RespException() {
            super();
        }

        public static RespException of(String... msg) {
            StringBuilder content = new StringBuilder(16);
            for (String s : msg) {
                content.append(s);
            }
            return new RespException(content.toString());
        }

        public RespException(String s) {
            super(s);
        }
    }
}
