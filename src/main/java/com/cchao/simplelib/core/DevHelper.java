package com.cchao.simplelib.core;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.R;
import com.cchao.simplelib.util.CallBacks;
import com.cchao.simplelib.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发者帮助类
 *
 * @author cchao
 * @since 2019-11-18
 */
public class DevHelper {

    public static boolean isDev() {
        return PrefHelper.getBoolean(Const.Pref.Dev_Mode, false);
    }

    /**
     * 显示开发者功能选项 对话框
     *
     * @param mOptions 弹出选项
     */
    public static void showDevOptionDialog(Context context, String devSignal, List<DevOptionItem> mOptions) {
        /*
         * 无暗号，或暗号已经验证成功已是开发者
         */
        if (isDev() || StringHelper.isEmpty(devSignal)) {
            showDevOptionDialog(context, mOptions);
            return;
        }

        EditText editText = new EditText(context);
        new AlertDialog.Builder(context)
            .setTitle("输入开发者暗号")
            .setView(editText)
            .setPositiveButton(R.string.confirm, (dialog, which) -> {
                if (!editText.getText().toString().equals(devSignal)) {
                    return;
                }

                PrefHelper.putBoolean(Const.Pref.Dev_Mode, true);
                showDevOptionDialog(context, mOptions);
            }).show();
    }


    /**
     * 显示开发者功能选项 对话框
     *
     * @param mOptions 弹出选项
     */
    public static void showDevOptionDialog(Context context, List<DevOptionItem> mOptions) {
        List<String> mOptionNames = new ArrayList<>();
        for (DevOptionItem option : mOptions) {
            mOptionNames.add(option.getName());
        }
        // 开发者选项
        UiHelper.showItemsDialog(context, "开发者选项", mOptionNames.toArray(new String[]{})
            , (dialog2, which) -> {
                DevOptionItem optionItem = mOptions.get(which);
                // 如果不需要输入
                if (!optionItem.isNeedInput()) {
                    optionItem.mCallBack.onCallBack("");
                    return;
                }
                EditText editText = new EditText(context);
                new AlertDialog.Builder(context)
                    .setTitle(optionItem.getInputHint())
                    .setView(editText)
                    .setPositiveButton(R.string.confirm, (dialog, which2) -> {
                        optionItem.mCallBack.onCallBack(editText.getText().toString());
                    }).show();
            });
    }

    /**
     * 开发者工具 配置项，建造者
     */
    public static class Config {
        /**
         * 入口暗号
         */
        public static String mDevSignal;
        /**
         * 是否开发者模式
         */
        public static String mIsDevMode;

        public static Config get() {
            return new Config();
        }

        public static String getmDevSignal() {
            return mDevSignal;
        }

        public static void setmDevSignal(String mDevSignal) {
            Config.mDevSignal = mDevSignal;
        }

        public static String getmIsDevMode() {
            return mIsDevMode;
        }

        public static void setmIsDevMode(String mIsDevMode) {
            Config.mIsDevMode = mIsDevMode;
        }
    }

    /**
     * 开发选项实体，建造者
     */
    public static class DevOptionItem {
        /**
         * 是否需要输入内容
         */
        boolean mNeedInput;
        /**
         * 输入提醒
         */
        String mInputHint;
        /**
         * 选项名
         */
        String mName;
        CallBacks.Str mCallBack;

        public static DevOptionItem get() {
            return new DevOptionItem();
        }

        public boolean isNeedInput() {
            return mNeedInput;
        }

        public DevOptionItem setNeedInput(boolean needInput) {
            mNeedInput = needInput;
            return this;
        }

        public String getInputHint() {
            return mInputHint;
        }

        public DevOptionItem setInputHint(String inputHint) {
            mInputHint = inputHint;
            return this;
        }

        public String getName() {
            return mName;
        }

        public DevOptionItem setName(String name) {
            mName = name;
            return this;
        }

        public CallBacks.Str getCallBack() {
            return mCallBack;
        }

        public DevOptionItem setCallBack(CallBacks.Str callBack) {
            mCallBack = callBack;
            return this;
        }
    }
}
