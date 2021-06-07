package com.cchao.simplelib.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.R;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.databinding.WebViewFragmentBinding;
import com.cchao.simplelib.http.OkHttpHelper;
import com.cchao.simplelib.ui.fragment.BaseStatefulFragment;
import com.cchao.simplelib.util.UrlUtil;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * 包含 webView 的Fragment，有进度条，出错会调用 switch Error
 *
 * @author cchao
 * @version 2019-05-10.
 */
public class WebViewFragment extends BaseStatefulFragment<WebViewFragmentBinding> implements BackAble {

    public WebView mWebView;
    protected ProgressBar mProgressBar;

    /**
     * 初始进入时 加载的 url
     */
    protected String mInitLoadWebUrl;
    /**
     * 当前加载的 Url
     */
    public String mCurLoadWebUrl;
    protected ValueCallback<Uri[]> mFilesChooserCallBack;
    protected ValueCallback<Uri> mFileChooserCallBack;
    protected WebViewHandler mWebViewHandler;

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
        RxBus.get().toObservable(commonEvent -> {
            switch (commonEvent.getCode()) {
                case Const.Event.X5_File_Chooser:
                    if (mFilesChooserCallBack != null) {
                        mFilesChooserCallBack.onReceiveValue(commonEvent.getBean() != null ? new Uri[]{commonEvent.getBean()} : null);
                    }

                    if (mFileChooserCallBack != null) {
                        mFileChooserCallBack.onReceiveValue(commonEvent.getBean());
                    }
                    mFilesChooserCallBack = null;
                    mFileChooserCallBack = null;
                    break;
            }
        });
    }

    @Override
    public void onLoadData() {
        syncCookies(mCurLoadWebUrl);
        mCurLoadWebUrl = appendBusinessParams(mCurLoadWebUrl);

        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }

        switchView(CONTENT);
        mWebView.loadUrl(mCurLoadWebUrl);
        Logs.logEvent("WebViewFragment onLoadData", mCurLoadWebUrl);
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
        String cacheDir = mContext.getCacheDir().getAbsolutePath();
        settings.setAppCacheMaxSize(1024*1024*80);
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheEnabled(true);

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        // 设置支持DomStorage
        settings.setDomStorageEnabled(true);

        mWebView.setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && LibCore.getInfo().isDebug()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        if (mWebView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            mWebView.getX5WebViewExtension()
                .invokeMiscMethod("setVideoParams", data);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mWebView != null) {
            mWebView.destroy();
        }
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
            Logs.logEvent("WebViewFragment onReceivedError", "url " + failingUrl + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, com.tencent.smtt.export.external.interfaces.SslError error) {
            // 开发者忽略证书的认证
            if (LibCore.getInfo().isDebug()) {
                handler.proceed();
            }
            Logs.logEvent("WebViewFragment sslError", view.getUrl());

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
            url = appendBusinessParams(url);
            Logs.d("WebViewFragment load url " + url);
            // 被 handler 拦截
            if (mWebViewHandler != null && mWebViewHandler.shouldOverrideUrlLoading(view, url)) {
                return true;
            }
            // 自定义协议，跳转交由系统决定
            if (!url.startsWith("http")) {
                try {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
//                    Logs.logException(e);
                }
                return true;
            }
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
        public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
            geolocationPermissionsCallback.invoke(s, true, false);
            super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
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

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
            mFileChooserCallBack = valueCallback;
            openFileChooseProcess();
            super.openFileChooser(valueCallback, s, s1);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            mFilesChooserCallBack = valueCallback;
            openFileChooseProcess();
            return true;
        }
    }

    void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "选择文件"), 0);
    }

    public void setWebViewHandler(WebViewHandler handler) {
        mWebViewHandler = handler;
    }

    public interface WebViewHandler {
        /**
         * 拦截规则
         */
        boolean shouldOverrideUrlLoading(WebView view, String url);

        /**
         * 追加通用参数
         */
        default boolean needAppendCommonParam() {
            return true;
        }
    }

    public String appendBusinessParams(String url) {
        if (mWebViewHandler == null || !mWebViewHandler.needAppendCommonParam()) {
            return url;
        }
        if (!url.startsWith("http")) {
            return url;
        }
        // 避免 vue的 #结尾，使用 zz替换，得到结果再还原
        url = url.replaceAll("/#/", "/zzz/");
        url = UrlUtil.appendParameter(url, LibCore.getInfo().getWebParams());
        return url.replaceAll("/zzz/", "/#/");
    }
}
