package com.cchao.simplelib;

import java.nio.charset.Charset;

/**
 * 类库所使用的常量
 *
 * @author cchao
 * @version 2019/4/9.
 */
public class Const {
    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    public static final String STRING_UTF_8 = "utf-8";

    public enum TitleStyle {
        title, ToolBar
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

}
