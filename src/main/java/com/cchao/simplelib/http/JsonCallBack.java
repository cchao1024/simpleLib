package com.cchao.simplelib.http;

import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author cchao
 * @version 18-5-20.
 */
public abstract class JsonCallBack implements Callback {
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
            JSONObject jsonObject = new JSONObject(body);
            onRespond(jsonObject);
        } catch (Exception e) {
            onError(call, e);
        }
    }

    public abstract void onRespond(JSONObject jsonObj) throws Exception;

    public void onError(Call call, Exception e) {
        ExceptionCollect.logException(e);
    }
}
