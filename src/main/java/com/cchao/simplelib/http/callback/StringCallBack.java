package com.cchao.simplelib.http.callback;

import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.util.StringHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求的 字符串 响应
 *
 * @author cchao
 * @version 18-5-20.
 */
public abstract class StringCallBack implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {
        onError(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                onError(call, new IOException("responseBody is null"));
                return;
            }
            String body = responseBody.string();
            if (StringHelper.isEmpty(body)) {
                onError(call, new IOException("responseBody.string() is null"));
                return;
            }
            UiHelper.runOnUiThread(() -> {
                try {
                    onRespond(call, body);
                } catch (Exception e) {
                    Logs.logException(e);
                }
            });
        } catch (Exception e) {
            UiHelper.runOnUiThread(() -> {
                onError(call, e);
            });
        }
    }

    public abstract void onRespond(Call call, String respStr) throws Exception;

    public void onError(Call call, Exception e) {
        Logs.logException(e);
    }
}
