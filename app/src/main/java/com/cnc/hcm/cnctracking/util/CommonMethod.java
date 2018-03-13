package com.cnc.hcm.cnctracking.util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by giapmn on 9/15/17.
 */

public class CommonMethod {

    public static String formatTimeFromServerToString(String inputTime) {
        SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL);
        TimeZone timeZoneDefault = TimeZone.getDefault();
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneDefault.getDisplayName());
        format.setTimeZone(timeZone);
        Date date = null;
        try {
            date = format.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = CommonMethod.formatDateToString(date.getTime());
        return time;
    }

    public static String formatTimeAppointmentDateBeforThirtyMinute(String inputTime) {
        SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL);
        TimeZone timeZone = TimeZone.getDefault();
        format.setTimeZone(TimeZone.getTimeZone(timeZone.getDisplayName()));
        Date date = null;
        try {
            date = format.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long thirtyMinute = TimeUnit.MINUTES.toMillis(30);
        long timeAfterMinus = date.getTime() - thirtyMinute;
        date.setTime(timeAfterMinus);
        String time = CommonMethod.formatDateToString(date.getTime());
        return time;
    }

    public static Date formatTimeFromServerToDate(String inputTime) {
        SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL);
        TimeZone timeZone = TimeZone.getDefault();
        format.setTimeZone(TimeZone.getTimeZone(timeZone.getDisplayName()));
        Date date = null;
        try {
            date = format.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date formatTimeFromServerToDate2(String inputTime) {
        SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL2);
        TimeZone timeZone = TimeZone.getDefault();
        format.setTimeZone(TimeZone.getTimeZone(timeZone.getDisplayName()));
        Date date = null;
        try {
            date = format.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatTimeToString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(date);
    }

    public static String formatFullTimeToString(Date date) {
        return new SimpleDateFormat(Conts.FORMAT_DATE_FULL).format(date);
    }

    public static String formatDateToString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String formatCurrency(long number) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        return (format.format(number) + Conts.BLANK).replaceAll(",", ".");
    }

    public static String formatMoney(int number) {
        DecimalFormat format = new DecimalFormat("###,###");
        return (format.format(number) + Conts.BLANK).replaceAll(",", ".");
    }


    public static String formatTimeToMonth(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("MM").format(date);
    }

    public static String formatTimeToYear(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("yyyy").format(date);
    }

    public static String formatTimeStandand(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static Calendar getInstanceCalendar() {
        return Calendar.getInstance();
    }

    public static void actionFindWayInMapApp(Context context, double latitude_cur, double longitude_cur, double latitude, double longitude) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + latitude_cur + "," + longitude_cur + "&daddr=" + latitude + "," + longitude));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        context.startActivity(intent);
    }

    public static void actionCall(Context context, String phoneNo) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNo, null)));
    }

    public static void actionSMS(Context context, String phoneNo) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNo, null)));
    }

    public static void makeToast(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_LONG).show();
    }

    public static String[] getStartEndDate(int currentMonth, int years) {
        String[] arr = new String[2];
        Calendar calendar = getInstanceCalendar();
        String month = currentMonth < 9 ? ("0" + (currentMonth + 1)) : ((currentMonth + 1) + Conts.BLANK);
        String dateFirstOfMonthTemp = years + "-" + month + "-01" + Conts.FORMAT_TIME_FULL;
        SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL);
        Date dateFirstOfMonth = null;
        try {
            dateFirstOfMonth = format.parse(dateFirstOfMonthTemp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateFirstOfMonth != null) {
            ArrayList<String> arrDateOfMonth = new ArrayList<>();
            calendar.setTime(dateFirstOfMonth);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int myMonth = calendar.get(Calendar.MONTH);
            while (myMonth == calendar.get(Calendar.MONTH)) {
                arrDateOfMonth.add(format.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            String startDate = arrDateOfMonth.get(0);
            String endDate = arrDateOfMonth.get(arrDateOfMonth.size() - 1);
            arr[0] = startDate;
            arr[1] = endDate;
        }
        return arr;
    }

    public static Address getLocationFromLocationName(Context context, String locationName) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(locationName, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0);
        }
        return null;
    }

    public static boolean checkCurrentDay(String paramDay) {
        if (paramDay.contains(".")) {
            try {
                paramDay = paramDay.substring(0, paramDay.lastIndexOf(".")) + "Z";
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        boolean isToday = false;
        Calendar calendarParam = getInstanceCalendar();
        Date inputParam = formatTimeFromServerToDate(paramDay);
        if (inputParam != null) {
            calendarParam.setTime(inputParam);

            int yearParam = calendarParam.get(Calendar.YEAR);
            int monthParam = calendarParam.get(Calendar.MONTH);
            int dayParam = calendarParam.get(Calendar.DAY_OF_MONTH);

            Calendar calendar = getInstanceCalendar();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (yearParam == currentYear
                    && monthParam == currentMonth
                    && dayParam == currentDay) {
                isToday = true;
            }
        }
        return isToday;
    }

}