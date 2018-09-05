package com.cchao.simplelib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.cchao.simplelib.core.Logs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static String formatSecond(long millisUntilFinished) {
        StringBuffer buffer = new StringBuffer();

        long second = millisUntilFinished / 1000;
        if (second < 10) {
            buffer.append("0");
        }
        buffer.append(second);
        buffer.append(" S ");

        return buffer.toString();
    }

    public static String formatTime(long millisUntilFinished) {
        StringBuffer buffer = new StringBuffer();

        long day = millisUntilFinished / (24 * 60 * 60 * 1000);
        if (day < 10) {
            buffer.append("0");
        }
        buffer.append(day);
        buffer.append("D: ");

        long hour = millisUntilFinished % (24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        if (hour < 10) {
            buffer.append("0");
        }
        buffer.append(hour);
        buffer.append("H: ");

        long minute = millisUntilFinished % (60 * 60 * 1000) / (60 * 1000);
        if (minute < 10) {
            buffer.append("0");
        }
        buffer.append(minute);
        buffer.append("M: ");

        long second = millisUntilFinished % (60 * 1000) / (1000);
        if (second < 10) {
            buffer.append("0");
        }
        buffer.append(second);
        buffer.append("S: ");

        long seconds10 = (int) ((millisUntilFinished / 100) % 10);
        buffer.append(seconds10);
        buffer.append("MS");

        return buffer.toString();
    }

    // 判断是否同一天
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY
            && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis))
            / MILLIS_IN_DAY;
    }

    /**
     * 判断两个日期之间相差的天数
     *
     * @param fDate
     * @param tDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date tDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fDate);
        int fDay = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(tDate);
        int tDay = calendar.get(Calendar.DAY_OF_YEAR);
        return tDay - fDay;
    }

    public static String parseUnixTime(Date time) {
        return Long.toString(time.getTime() / 1000); // 转为秒
    }

    public static Date parseUnixDate(Context context, String timeStr) {
        try {
            long timestamp = Long.parseLong(timeStr) * 1000; // 转为秒

            return new Date(timestamp);
        } catch (Exception e) {
            Logs.e(e);

            return new Date();
        }
    }

    public static String formatUnixDate(Context context, long time) {
        try {
            return formatDate(context, time * 1000); // 转为秒
        } catch (Exception e) {
            Logs.e(e);

            return String.valueOf(time);
        }
    }

    public static String formatUnixDate(Context context, String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }

        return formatUnixDate(context, Long.parseLong(time));
    }

    public static String formatUnixTime(Context context, String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }

        long value = 0;
        try {
            value = Long.parseLong(time);
        } catch (NumberFormatException e) {
            return time;
        }

        return (value == 0) ? "" : formatUnixTime(context, value);
    }

    public static String formatUnixTime(Context context, long time) {
        try {
            return formatTime(context, time * 1000); // 转为秒
        } catch (Exception e) {
            Logs.e(e);

            return String.valueOf(time);
        }
    }

    public static String formatDate(Context context, long time) {
        return formatDate(context, new Date(time));
    }

    public static String formatTime(Context context, long time) {
        return formatTime(context, new Date(time));
    }

    /**
     * 参照系统格式化时间
     *
     * @param context
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Context context, Date date) {
        DateFormat dateFormat = android.text.format.DateFormat
            .getDateFormat(context);
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatMediumTime(Context context, Date date) {
        DateFormat timeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return timeFormat.format(date);
    }

    public static String formatTime(Context context, Date date) {
        DateFormat dateFormat = android.text.format.DateFormat
            .getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat
            .getTimeFormat(context);

        StringBuffer buff = new StringBuffer();
        buff.append(dateFormat.format(date));
        buff.append(" ");
        buff.append(timeFormat.format(date));
        String string = buff.toString();
        if (string.contains("上午")) {
            string = string.replace("上午", "");
            string += " AM";
        }
        if (string.contains("下午")) {
            string = string.replace("下午", "");
            string += " PM";
        }
        return string;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            return format.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}
