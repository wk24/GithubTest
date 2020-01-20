package com.example.mylibrary;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fenghl on 2018/6/8.
 */

public class DateUtils {
    private static SimpleDateFormat m = new SimpleDateFormat("MM", Locale.getDefault());
    private static SimpleDateFormat d = new SimpleDateFormat("dd", Locale.getDefault());
    private static SimpleDateFormat md = new SimpleDateFormat("MM-dd", Locale.getDefault());
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat ymdDot = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
    private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat ymdhmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private static SimpleDateFormat hm = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static SimpleDateFormat mdhm = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
    private static SimpleDateFormat mdhmLink = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public static String getYmd(long timeInMills) {
        return ymd.format(new Date(timeInMills));
    }

    public static String getYmdDot(long timeInMills) {
        return ymdDot.format(new Date(timeInMills));
    }

    public static String getYmdhms(long timeInMills) {
        return ymdhms.format(new Date(timeInMills));
    }

    public static String getYmdhmsS(long timeInMills) {
        return ymdhmss.format(new Date(timeInMills));
    }

    public static String getYmdhm(long timeInMills) {
        return ymdhm.format(new Date(timeInMills));
    }

    public static String getHm(long timeInMills) {
        return hm.format(new Date(timeInMills));
    }

    public static String getMd(long timeInMills) {
        return md.format(new Date(timeInMills));
    }

    public static String getMdhm(long timeInMills) {
        return mdhm.format(new Date(timeInMills));
    }

    public static String getMdhmLink(long timeInMills) {
        return mdhmLink.format(new Date(timeInMills));
    }

    public static String getM(long timeInMills) {
        return m.format(new Date(timeInMills));
    }

    public static String getD(long timeInMills) {
        return d.format(new Date(timeInMills));
    }

    public static boolean isToday(long timeInMills) {
        String dest = getYmd(timeInMills);
        String now = getYmd(Calendar.getInstance().getTimeInMillis());
        return dest.equals(now);
    }

    public static boolean isSameDay(long aMills, long bMills) {
        String aDay = getYmd(aMills);
        String bDay = getYmd(bMills);
        return aDay.equals(bDay);
    }

    public static int getYear(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDaysInMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static int getWeek(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static long getFirstOfMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }


    /**
     * 新时间老时间对比
     *
     * @param newTime 新时间
     * @param oldTime 老时间
     * @return boolean 是否有更新
     */
    public static boolean timeNewCompare(String newTime, String oldTime) {
        try {
            Date beginTime = ymdhms.parse(newTime);
            Date endTime = ymdhms.parse(oldTime);
            return endTime.getTime() - beginTime.getTime() > 0L;
        } catch (Exception var5) {
            var5.printStackTrace();
            return true;
        }
    }


    /**
     * 新时间老时间对比
     *
     * @param sTime 开始时间
     * @param eTime 结束时间
     * @return boolean 是否有更新
     */
    public static long timeCompare(String sTime, String eTime) {
        try {
            Date beginTime = ymdhms.parse(sTime);
            Date endTime = ymdhms.parse(eTime);
            return endTime.getTime() - beginTime.getTime();
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0;
        }
    }

    /**
     * 把一个毫秒的时长格式化为字符串，如果毫秒有包含小时，则格式化为：时:分:秒，如01:30:49，否则格式化为分:秒，如30:49
     *
     * @param duration long
     * @return
     */
    public static CharSequence formatMillis(long duration) {
        Calendar calendar = Calendar.getInstance(); // 以当前的时间创建一个日历对象
        calendar.clear();   // 清空时间，保留到：（格林尼治标准时间）1970 年 1 月 1 日 00:00:00
        calendar.add(Calendar.MILLISECOND, (int) duration); // 把毫秒值塞到日历对象中
        long oneHourMillis = 1 * 60 * 60 * 1000;    // 1小时对应的毫秒值
        boolean hasHour = duration / oneHourMillis > 0;
        CharSequence inFormat = hasHour ? "kk:mm:ss" : "mm:ss";
        return DateFormat.format(inFormat, calendar);
    }


}
