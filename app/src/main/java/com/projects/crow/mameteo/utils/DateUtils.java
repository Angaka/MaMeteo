package com.projects.crow.mameteo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Venom on 13/09/2017.
 */

public class DateUtils {

    private static final String TAG = "DateUtils";

    public static String convertTimestampInHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(timestamp * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date dateHour = calendar.getTime();
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(dateHour);
    }

    public static Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public static Date getNextHour() {
        Date currentTime = Calendar.getInstance().getTime();
        return toNearestNextHour(currentTime);
    }

    private static Date toNearestNextHour(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        if (c.get(Calendar.MINUTE) >= 30)
            c.add(Calendar.HOUR, 1);

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }
}
