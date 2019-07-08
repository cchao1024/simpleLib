package com.cchao.simplelib.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.cchao.simplelib.Const;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.PrefHelper;

import java.util.Locale;

/**
 * 国际化 多语言工具类
 *
 * @author cchao
 * @version 2019-07-08.
 */
public class LanguageUtil {
    public static String Cur_Language = "zh_CN";
    public static String Default_Language = "zh_CN";

    /**
     * 改变语言
     */
    public static void changeLanguage(String to) {
        Locale locale = new Locale(to);
        Resources resources = LibCore.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = new Configuration();
        config.locale = locale;
        // 应用用户选择语言
        // 参考 https://medium.com/@zhangqichuan/rtl-support-in-android-898e11f31561
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
        }
        Locale.setDefault(locale);
        resources.updateConfiguration(config, dm);
        PrefHelper.putString(Const.Pref.Language, to);
        Cur_Language = to;
    }


    /**
     * 返回系统的语言 en_GB
     */
    public static String getSystemLang() {
        Resources resources = LibCore.getContext().getResources();
        Configuration config = resources.getConfiguration();
        Locale locale = config.locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = config.getLocales().get(0);
        }
        String language = locale.getLanguage();
        if (StringHelper.isEmpty(language)) {
            return Default_Language;
        }
        final String country = locale.getCountry();
        if (StringHelper.isNotEmpty(country)) {
            language = language.toLowerCase(locale) + "_" + country.toUpperCase(locale);
            return language;
        }
        return Default_Language;
    }

    /**
     * 设置app语言
     */
    public static void init() {
        Cur_Language = getSystemLang();
        String languageAbbr = PrefHelper.getString(Const.Pref.Language, Default_Language);
        //如果已经设置过语言 ，就修改成设置的语言
        if (!languageAbbr.equalsIgnoreCase(Default_Language)) {
            changeLanguage(languageAbbr);
        }
    }
}
