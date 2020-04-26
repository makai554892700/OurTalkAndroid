package www.mys.com.ourtalkandroid.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class TimeUtils {


    private static final String HOURE_MARK = ":";
    private static final String MSEC_MARK = ".";
    private static final String SPLIT_HOURE_MARK = "\\:";
    private static final String SPLIT_MSEC_MARK = "\\.";
    private static final String TIME_REG = "^(\\d+)(" + HOURE_MARK + "[0-5][0-9]){2}" + MSEC_MARK + "(\\d{0,3})$";

    public static long getDayInt(long time) {
        return time % (24 * 60 * 60 * 1000);
    }

    public static long getDayInt(Date time) {
        return time == null ? -1 : time.getTime() % (24 * 60 * 60 * 1000);
    }

    public static int getDayInt() {
        Date date = new Date();
        return Integer.parseInt(getTimeZoneDateString(date, date.getTimezoneOffset(), TimeEnum.FORMAT_INT_DAY));
    }

    public static int getTimeByStr(String str) {
        if (str == null || !Pattern.matches(TIME_REG, str)) {
            LogUtils.log("str is null.or str is not matches.str=" + str);
            return 0;
        }
        String[] tempStr = str.split(SPLIT_HOURE_MARK);
        String[] tempStr2 = tempStr[2].split(SPLIT_MSEC_MARK);
        return (Integer.parseInt(tempStr[0]) * 3600 +
                Integer.parseInt(tempStr[1]) * 60 +
                Integer.parseInt(tempStr2[0])) * 1000 + Integer.parseInt(tempStr2[1]);
    }

    public static String getTimeStr(Long time) {
        if (time == null) {
            return "unknow";
        }
        return getTimeZoneDateString(new Date(time), 8
                , TimeEnum.FORMAT_MINUTE);
    }

    public static Date getDateByStr(String str, TimeEnum timeEnum) {
        if (str == null || timeEnum == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(timeEnum.getStr());
        try {
            return sdf.parse(str);
        } catch (Exception e) {
            LogUtils.log("e=" + e);
        }
        return null;
    }

    public static Date getDateAddMonth(Date date, int day) {
        if (day == 0) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, day);
        return calendar.getTime();
    }

    public static Date getDateAddDay(Date date, int day) {
        if (day == 0) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date getDateAddHour(Date date, int day) {
        if (day == 0) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, day);
        return calendar.getTime();
    }

    public static Date getDateAddMinutes(Date date, int day) {
        if (day == 0) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, day);
        return calendar.getTime();
    }

    public static String getTimeZoneDateString(Date date, float timeZoneOffset, TimeEnum timeEnum) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(timeEnum.getStr());
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static int sortDESC(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date1.getTime() < date2.getTime()) {
                return -1;
            }
            if (date1.getTime() > date2.getTime()) {
                return 1;
            }
        }
        return 0;
    }

}
