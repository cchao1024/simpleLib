package com.cchao.simplelib.ui.web;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.R;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.databinding.WebViewActivityBinding;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

import java.util.List;

/**
 * webView 实现，包裹 webFragment
 * 需要传入初始化参数  【 Web_View_Url  Web_View_Tile 】
 *
 * @author cchao
 * @version 2019-05-10.
 */
public class WebViewActivity extends BaseTitleBarActivity<WebViewActivityBinding> {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果是文件选择
        if (resultCode == RESULT_OK) {
            //给文件选择的ValueCallback设置onReceiveValue值
            RxBus.get().postEvent(Const.Event.X5_File_Chooser, data.getData());
        } else {
            //给文件选择的ValueCallback设置null值
            RxBus.get().postEvent(Const.Event.X5_File_Chooser, null);
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof BackAble) {
                if (((BackAble) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
