package com.cchao.simplelib.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.databinding.WebViewFragmentBinding;
import com.cchao.simplelib.http.OkHttpHelper;
import com.cchao.simplelib.ui.fragment.BaseStatefulFragment;
import com.cchao.simplelib.util.UrlUtil;

import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 包含 webView 的Fragment，有进度条，出错会调用 switch Error
 *
 * @author cchao
 * @version 2019-05-10.
 */
public class WebViewFragment extends BaseStatefulFragment<WebViewFragmentBinding> implements BackAble {

    protected WebView mWebView;
    protected ProgressBar mProgressBar;

    /**
     * 初始进入时 加载的 url
     */
    protected String mInitLoadWebUrl;
    /**
     * 当前加载的 Url
     */
    protected String mCurLoadWebUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.web_view_fragment;
    }

    @Override
    protected void initEventAndData() {
        mWebView = mDataBind.webView;
        mProgressBar = mDataBind.progressBar;
        initExtra();
        initWebView();
        onLoadData();
    }

    @Override
    protected void onLoadData() {
        syncCookies(mCurLoadWebUrl);
        for (Map.Entry<String, String> entry : Const.Req_Params.entrySet()) {
            // 追加AppBuild
            if (!mCurLoadWebUrl.contains(entry.getKey())) {
                mCurLoadWebUrl = UrlUtil.appendParament(mCurLoadWebUrl, entry.getKey(), entry.getValue());
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }
        switchView(CONTENT);

        mWebView.loadUrl(mCurLoadWebUrl);
        Logs.logEvent("加载Url", mCurLoadWebUrl);
    }

    protected void initExtra() {
        if (getActivity() == null) {
            return;
        }
        Bundle data = getActivity().getIntent().getExtras();
        if (data == null) {
            throw new IllegalArgumentException("Please put extra Web url");
        }
        mInitLoadWebUrl = data.getString(Const.Extra.Web_View_Url);
        mCurLoadWebUrl = mInitLoadWebUrl;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        // ZoomControls（就是放大放小按钮） 是个渐变的过程（渐变未完成就destroy）会导致View not attached to window manager
        settings.setDisplayZoomControls(false);
        // 设置定位的数据库路径
        settings.setDatabaseEnabled(true);
        String dir = mContext.getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);
        // 启用地理定位
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        // 设置支持DomStorage
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && LibCore.getInfo().isDebug()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.setWebViewClient(new CustomWebViewClient());
    }

    /**
     * 同步cookie
     */
    public void syncCookies(String url) {
        try {
            CookieSyncManager.createInstance(LibCore.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            List<Cookie> cookieList = OkHttpHelper.getCookies(HttpUrl.parse(url));
            for (Cookie cookie : cookieList) {
                cookieManager.setCookie(url, cookie.toString());
            }
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            Logs.logException(e);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public class CustomWebViewClient extends WebViewClient {

        public CustomWebViewClient() {
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // https://developer.android.com/reference/android/webkit/WebViewClient.html#ERROR_TIMEOUT
            // 是这些 code 就切换到出错页
            if (errorCode == -5 || errorCode == -1 || errorCode == -6 || errorCode == -8) {
                switchView(NET_ERROR);
            }
            Logs.logEvent("WebView onReceivedError", "url " + failingUrl + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            // 开发者忽略证书的认证
            if (LibCore.getInfo().isDebug()) {
                handler.proceed();
            }
            Logs.logEvent("webView sslError", view.getUrl());

            // 弹窗询问是否继续
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
                default:
                    break;
            }
            message += " Do you want to continue anyway?";
            builder.setTitle("SSL Certificate Error")
                .setMessage(message)
                .setPositiveButton("continue", (dialog, which) -> handler.proceed())
                .setNegativeButton("cancel", (dialog, which) -> handler.cancel())
                .setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            syncCookies(url);
            mCurLoadWebUrl = url;
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    public class CustomWebChromeClient extends WebChromeClient {

        public CustomWebChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgressBar == null) {
                return;
            }
            if (progress > 20) {
                mProgressBar.setProgress(progress);
            }

            UiHelper.setVisibleElseGone(mProgressBar, progress < 100);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> result.confirm())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                .create()
                .show();

            return true;
        }
    }
}
