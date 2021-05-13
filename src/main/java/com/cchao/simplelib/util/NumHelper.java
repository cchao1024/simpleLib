package com.cchao.simplelib.util;

import java.math.BigDecimal;
import java.time.temporal.ValueRange;
import java.util.Locale;

/**
 * 数值计算
 *
 * @author cch
 * @version 2020/9/15
 */
public class NumHelper {

    /**
     * 保留5位小数
     */
    public static String float5(float value) {
       return String.format(Locale.CHINESE, "%.5f", value);
    }

    /**
     * 保留5位小数
     */
    public static String float5StripZero(float v) {
        BigDecimal value = new BigDecimal(float5(v));
        BigDecimal noZeros = value.stripTrailingZeros();
        return noZeros.toPlainString();
    }

    /**
     * 保留 2位小数
     */
    public static String float2(float value) {
       return String.format(Locale.CHINESE, "%.2f", value);
    }
}
