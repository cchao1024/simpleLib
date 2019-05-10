package com.cchao.simplelib.ui.web;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.R;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

/**
 * webView 实现，包裹 webFragment
 * @author cchao
 * @version 2019-05-10.
 */
public class WebViewActivity extends BaseTitleBarActivity {


    @Override
    protected int getLayout() {
        return R.layout.web_view_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleText(getIntent().getStringExtra(Const.Extra.Web_View_Tile));
    }

    @Override
    protected void onLoadData() {

    }
}