package com.cchao.simplelib.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.cchao.simplelib.LibCore;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 页面跳转路由，，页面跳转都经过他统一处理，，后期可以通过判断页面类型满足权限判断、登录状态、埋点需求
 * 注意链式的添加跳转目的页需要的参数
 *
 * @author cchao
 * @version 2017.11.2.
 */
public class Router {

    public static Map<String, Long> mHistoryMap = new HashMap<>();

    /**
     * 这里对跳转进行各种处理，拦截，记录
     *
     * @param from       from
     * @param toActivity to
     */
    static void startRouter(Context from, Class toActivity, BundleHelper bundleHelper) {
        String logStr = from.getClass().getSimpleName() + " >>> " + toActivity.getName()
            + "bundle :" + bundleHelper.mBundle.toString();
        Logs.logEvent("Router", logStr);
        // 记录到历史 router ，用于防抖动点击
        if (mHistoryMap.containsKey(logStr) && (System.currentTimeMillis() - mHistoryMap.get(logStr)) < bundleHelper.mThrottleTime) {
            Logs.i("Router 抖动", logStr);
            return;
        }
        mHistoryMap.put(logStr, System.currentTimeMillis());

        // 需要登录，但未登录
        if (!bundleHelper.mTurnStatusMatch) {
            Logs.logEvent("Router 未满足跳转状态", logStr);
            return;
        }

        Intent intent = new Intent(from, toActivity);
        intent.putExtras(bundleHelper.mBundle);

        if (bundleHelper.mInNewTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        // 如果有 requestCode 则标识需要 startActivityForResult
        if (bundleHelper.mRequestCode == -1) {
            from.startActivity(intent);
        } else {
            ((Activity) from).startActivityForResult(intent, bundleHelper.mRequestCode);
        }
    }

    /**
     * 携带参数的跳转
     *
     * @param from       传入上下文
     * @param toActivity 目标Activity
     */
    public static BundleHelper turnTo(Context from, Class toActivity) {
        return new BundleHelper(from, toActivity);
    }

    public static class BundleHelper {
        public Bundle mBundle = new Bundle();
        Context mFrom;
        // 跳转状态是否满足
        boolean mTurnStatusMatch = true;
        boolean mInNewTask;
        int mRequestCode = -1;
        int mThrottleTime = 800;
        Class mToActivity;

        public BundleHelper(Context from, Class toActivity) {
            mFrom = from;
            mToActivity = toActivity;
        }

        public void start() {
            Router.startRouter(mFrom, mToActivity, this);
        }

        public void startInNewTask() {
            mInNewTask = true;
            Router.startRouter(mFrom, mToActivity, this);
        }

        public void startForResult(int requestCode) {
            mRequestCode = requestCode;
            Router.startRouter(mFrom, mToActivity, this);
        }

        /**
         * 需要复写 LibCore 的 isLogin 来进行业务判断，不复写默认为 true
         *
         * @param isNeedLogin 不满足 是否需要跳转登录
         */
        public BundleHelper checkLogin(boolean isNeedLogin) {
            boolean result = LibCore.getInfo().getRouterConfig().checkLogin(mFrom, isNeedLogin);
            if (!result) {
                mTurnStatusMatch = false;
            }
            return this;
        }

        /**
         * 需要复写 LibCore 的 checkStatus 来进行业务判断，不复写默认为 true
         *
         * @param isNeedAuthorize 不满足 是否需要跳转授权页
         */
        public BundleHelper checkStatus(String status, boolean isNeedAuthorize) {
            boolean result = LibCore.getInfo().getRouterConfig().checkStatus(mFrom, status, isNeedAuthorize);
            if (!result) {
                mTurnStatusMatch = false;
            }
            return this;
        }

        // 设置 防抖动点击的间隔时间
        public BundleHelper setThrottleTime(int mThrottleTime) {
            this.mThrottleTime = mThrottleTime;
            return this;
        }

        // region 各个putExtra
        public BundleHelper putExtra(String name, String value) {
            mBundle.putString(name, value);
            return this;
        }

        public BundleHelper putExtra(String name, int value) {
            mBundle.putInt(name, value);
            return this;
        }

        public BundleHelper putExtra(String name, long value) {
            mBundle.putLong(name, value);
            return this;
        }

        public BundleHelper putExtra(String name, boolean value) {
            mBundle.putBoolean(name, value);
            return this;
        }
        //endregion

        // 外部调用
        public static BundleHelper get() {
            return new BundleHelper(null, null);
        }

        /**
         * setArguments fragment
         *
         * @param fragment fragment
         */
        public Fragment injectFragment(Fragment fragment) {
            fragment.setArguments(mBundle);
            return fragment;
        }

    }
}
