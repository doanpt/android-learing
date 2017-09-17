package com.google.foods.utils;

import android.text.format.Formatter;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by Android on 7/27/2017.
 */

public class CommonMethod {
    private static final String TAG = CommonMethod.class.getSimpleName();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static DecimalFormat decimalFormat = new DecimalFormat("#,###,### VND", new DecimalFormatSymbols());

//    static {
//        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
//        symbols.setGroupingSeparator(' ');
//        decimalFormat = new DecimalFormat("#,###,### VND", symbols);
//    }

    public static int getSinglePrice(int type, int orderQuantity) {
//        int result = 0;
//        switch (type) {
//            case CommonValue.VALUE_FOOD_30_VND:
//                result = CommonValue.VALUE_FOOD_30_VND * orderQuantity;
//                break;
//            case CommonValue.VALUE_FOOD_35_VND:
//                result = CommonValue.VALUE_FOOD_35_VND * orderQuantity;
//                break;
//            case CommonValue.VALUE_FOOD_40_VND:
//                result = CommonValue.VALUE_FOOD_40_VND * orderQuantity;
//                break;
//        }
//        return result;
        return type * orderQuantity;
    }

    public static String getFormatedPrice(int price) {
        return decimalFormat.format(price);
    }

    /**
     * Example:
     * String stringDate = CommonMethod.getDateTimeString();
     * Log.e(TAG, "stringDate: " + stringDate);
     * Date date = CommonMethod.convertStringToDate(stringDate);
     * Calendar calendar = Calendar.getInstance();
     * calendar.setTime(date);
     * Log.e(TAG, "stringDate, Trưa/Tối: " + (calendar.get(Calendar.AM_PM) == Calendar.AM ? "Trưa" : "Tối"));
     */

    public static String getDateTimeString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String getDateTimeString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    public static String getDateString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static Date convertStringToDate(String dateString) {
        Date date = Calendar.getInstance().getTime();
        try {
            date = SIMPLE_DATE_FORMAT.parse(dateString);
        } catch (ParseException ex) {
            Log.e(TAG, "convertStringToDate, parseException for: " + dateString, ex);
        }
        return date;
    }

    public static String convertMoneyToVND(long money){
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(money) + " VND";
    }
}
