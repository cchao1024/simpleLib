package com.cchao.simplelib;

import com.cchao.simplelib.core.AndroidHelper;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 类库所使用的常量
 *
 * @author cchao
 * @version 2019/4/9.
 */
public class Const {
    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    public static final String STRING_UTF_8 = "utf-8";
    public static final Map<String, String> Req_Params = new HashMap<>();

    static {
        Req_Params.put("deviceNo", AndroidHelper.getDeviceNum());
        Req_Params.put("appBuild", String.valueOf(LibCore.getInfo().getAppVersionCode()));
    }

    /**
     * title 的样式
     */
    public enum TitleStyle {
        // 自定义线性title
        title,
        // toolbar实现
        ToolBar
    }

    public interface RESP_TYPE {
        String JSON_ERROR = "JsonError";
        String JSON_EMPTY = "JsonEmpty";
        String RESP_500 = "Respond500";
        String RESP_404 = "Respond404";
        String RESP_503 = "Respond503";
        String RESP_FAIL = "RespondOtherFail";
        String API_TIME_OUT = "ApiTimeOut";
    }

    public interface Config {

        /**
         * api请求超时
         */
        int DEFAULT_TIMEOUT = 30;

        /**
         * 图片请求超时
         */
        int IMAGE_TIMEOUT = 60;

    }

    public interface Extra {

        /**
         * 加载webview的链接
         */
        String Web_View_Url = "web_view_url";
        String Web_View_Tile = "web_view_title";

    }

    public interface Pref {
        String Language= "language";
    }

}
