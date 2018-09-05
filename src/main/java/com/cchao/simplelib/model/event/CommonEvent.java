package com.cchao.simplelib.model.event;

/**
 * 通用的事件类 content为携带的字符串数据 , mBean 为携带的obj
 *
 * @author cchao
 * @version 2017.12.19.
 */
public class CommonEvent {
    private int mCode;
    private String mContent;
    private Object mBean;

    /**
     * 构造方法
     */
    private CommonEvent(int code, String content, Object bean) {
        this.mCode = code;
        this.mContent = content;
        this.mBean = bean;
    }

    /**
     * 返回一个通用事件
     *
     * @param code 唯一的code
     * @return event
     */
    public static CommonEvent newEvent(int code) {
        return new CommonEvent(code, "", null);
    }

    public static CommonEvent newEvent(int code, String content) {
        return new CommonEvent(code, content, null);
    }

    public static CommonEvent newEvent(int code, Object bean) {
        return new CommonEvent(code, "", bean);
    }

    public String getContent() {
        return mContent;
    }

    public <T> T getBean() {
        return (T) mBean;
    }

    public int getCode() {
        return mCode;
    }
}

