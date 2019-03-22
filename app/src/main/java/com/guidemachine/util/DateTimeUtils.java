package com.guidemachine.util;


import com.guidemachine.constant.SF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** 日期时间 工具类 */
public class DateTimeUtils {

    private static DateTimeUtils mDateTimeUtils; // 本类实例

    private DateTimeUtils() {
    }

    /** 获取 DateTimeUtils 的实例 */
    public static DateTimeUtils getInstance() {
        if (mDateTimeUtils == null) {
            synchronized (DateTimeUtils.class) {
                if (mDateTimeUtils == null) {
                    mDateTimeUtils = new DateTimeUtils();
                }
            }
        }
        return mDateTimeUtils;
    }

    // =============================================================================================

    /** 获取用户现在时间 【format = 格式】 */
    public String getNowTime(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    /** 获取 时间毫秒 【add = 需要 加 的数】【sub = 需要 减 的数】【不需加减则传 0】 */
    public long getNowTime(long add, long sub) {
        return System.currentTimeMillis() + add - sub;
    }
    // =============================================================================================


    // =============================================================================================

    /** 字符串日期 TO 时间戳 */
    public long getDateToTimestamp(String format, String date) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 时间戳 TO 字符串日期 */
    public String getTimestampToDate(String format, long timestamp) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(timestamp);
    }
    // =============================================================================================


    // =============================================================================================

    /** 整型 年月日 TO 字符串日期 */
    public String getTimesToDate(String format, int year, int month, int day) {
        return getTimesToDate(format, year, month, day, 0, 0);
    }

    /** 整型 年月日时分 TO 字符串日期 */
    public String getTimesToDate(String format, int year, int month, int day, int hourOfDay, int minute) {
        return getTimesToDate(format, year, month, day, hourOfDay, minute, 0);
    }

    /** 整型 年月日时分秒 TO 字符串日期 */
    public String getTimesToDate(String format, int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hourOfDay, minute, second);
        return getTimesToDate(format, calendar);
    }

    /** 整型 年月日时分秒 TO 字符串日期 */
    public String getTimesToDate(String format, Calendar calendar) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }
    // =============================================================================================


    // =============================================================================================

    /** 字符串日期 对比日期差；返回 是否超过指定值 */
    public boolean compareDateDifference(String format, String startTime, String endTime, long difference) {
        long startTimeL = getDateToTimestamp(format, startTime);
        long endTimeL = getDateToTimestamp(format, endTime);
        return compareDateDifference(startTimeL, endTimeL, difference);
    }

    /** 字符串日期 对比日期差；返回 是否超过指定值 */
    public boolean compareDateDifference(long startTime, long endTime, long difference) {
        return endTime - startTime > difference;
    }
    // =============================================================================================

    /** 字符串日期 对比；返回 是否正序 */
    public boolean compareTo(String format, String startTime, String endTime) {
        return getDateToTimestamp(format, startTime) <= getDateToTimestamp(format, endTime);
    }

    /** 获取某天指定时间点 【timestamp = 指点某天】【timePoint = 时间点/小时、分钟、秒 数】 */
    public long getCustomTimePoint(long timestamp, long timePoint) {
        String date = new SimpleDateFormat(SF.DT_003, Locale.getDefault()).format(timestamp);
        return getDateToTimestamp(SF.DT_003, date) + timePoint;
    }
    /** 时间戳转日期*/
    public String StringToTimes(String time,String type) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        long lt = new Long(time);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
