package com.cchao.simplelib.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Description: 页面跳转路由，，页面跳转都经过他统一处理，，后期可以通过判断页面类型满足权限判断、登录状态、埋点需求
 * 注意链式的添加跳转目的页需要的参数
 *
 * @author cchao
 * @version 2017.11.2.
 */
public class Router {

    /**
     * 这里对跳转进行各种处理，拦截，记录
     *
     * @param from       from
     * @param toActivity to
     * @param bundle     携带
     */
    static void startRouter(Context from, Class toActivity, Bundle bundle, int requestCode) {
        String logStr = from.getClass().getSimpleName() + " >>> " + toActivity.getName() + "bundle :" + bundle.toString();
        Logs.d("Router", logStr);

        Intent intent = new Intent(from, toActivity);
        intent.putExtras(bundle);
        if (requestCode == -1) {
            from.startActivity(intent);
        } else {
            ((Activity) from).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 携带参数的跳转
     *
     * @param from       传入上下文
     * @param toActivity 目标Activity
     */
    public static BundleDecorator turnTo(Context from, Class toActivity) {
        return new BundleDecorator(from, toActivity);
    }

    public static class BundleDecorator {
        Bundle mBundle = new Bundle();
        Context mFrom;
        Class mToActivity;

        public BundleDecorator(Context from, Class toActivity) {
            mFrom = from;
            mToActivity = toActivity;
        }

        public void start() {
            Router.startRouter(mFrom, mToActivity, mBundle, -1);
        }

        public void startForResult(int requestCode) {
            Router.startRouter(mFrom, mToActivity, mBundle, requestCode);
        }

        // region 各个putExtra
        public BundleDecorator putExtra(String name, String value) {
            mBundle.putString(name, value);
            return this;
        }

        public BundleDecorator putExtra(String name, int value) {
            mBundle.putInt(name, value);
            return this;
        }

        public BundleDecorator putExtra(String name, boolean value) {
            mBundle.putBoolean(name, value);
            return this;
        }
        //endregion
    }
}
