package com.prolificinteractive.materialcalendarview.until;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Hoang on 03/11/2018.
 */

public class CommonMethod {
    public static Calendar getInstanceCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(Conts.TIME_ZONE_VN));
    }
}
