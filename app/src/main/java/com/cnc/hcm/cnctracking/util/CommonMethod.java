package com.cnc.hcm.cnctracking.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by giapmn on 9/15/17.
 */

public class CommonMethod {

    public static String formatTimeToString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(date);
    }
}
