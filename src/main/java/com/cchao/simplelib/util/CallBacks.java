package com.cchao.simplelib.util;

/**
 * @author cchao
 * @version 11/1/18.
 */
public class CallBacks {

    public interface Bool{
        void onCallBack(boolean bool);
    }

    public interface Int{
        void onCallBack(int intValue);
    }

    public interface String{
        void onCallBack(String str);
    }
}
