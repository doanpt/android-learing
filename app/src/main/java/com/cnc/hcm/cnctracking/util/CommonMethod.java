package com.cnc.hcm.cnctracking.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.event.OnResultTimeDistance;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by giapmn on 9/15/17.
 */

public class CommonMethod {

    public static String formatTimeToString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(date);
    }

    public static String formatDateToString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String format(double number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(number) + Conts.BLANK;
    }


    public static String formatTimeToMonth(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("MM").format(date);
    }

    public static String formatTimeToYear(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("yyyy").format(date);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String getDestination(ArrayList<GetTaskListResult> arrayList) {
        String result = Conts.BLANK;
        if (arrayList.size() > Conts.DEFAULT_VALUE_INT_0) {
            StringBuilder builder = new StringBuilder();
            for (GetTaskListResult itemWork : arrayList) {
                GetTaskDetailResult.Result result1 = itemWork.result[0];
                builder.append(result1.customer.address.location.latitude + "," + result1.customer.address.location.longitude + "|");
            }
            result = builder.toString().substring(Conts.DEFAULT_VALUE_INT_0, builder.toString().lastIndexOf("|"));
        }
        return result;
    }

    public static void jsonRequestUpdateDistance(boolean isNetworkConected, double latiOrigin, double longtiOrigin, String destination, final OnResultTimeDistance onResultTimeDistance) {
        if (latiOrigin != 0.0 && longtiOrigin != 0.0 && !destination.equals(Conts.BLANK) && isNetworkConected) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                    + latiOrigin + "," + longtiOrigin + "&destinations=" + destination + "&key=AIzaSyD8Hp8WobhNtPUAPVJsU8mk5s_aRPa4lC4").build();
            try {
                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) {

                        try {
                            String myResponse = response.body().string();

                            JSONObject object = new JSONObject(myResponse);
                            JSONArray arrRow = object.getJSONArray(Conts.JSON_ELEMENT_ROWS);
                            if (arrRow.length() > 0) {
                                JSONObject ob = arrRow.getJSONObject(Conts.DEFAULT_VALUE_INT_0);
                                JSONArray arr = ob.getJSONArray(Conts.JSON_ELEMENT);
                                if (arr.length() > 0) {
                                    for (int index = Conts.DEFAULT_VALUE_INT_0; index < arr.length(); index++) {
                                        JSONObject objDistance = arr.getJSONObject(index).getJSONObject(Conts.JSON_ELEMENT_DISTANCE);
                                        JSONObject objDuration = arr.getJSONObject(index).getJSONObject(Conts.JSON_ELEMENT_DURATION);

                                        String distance = objDistance.getString(Conts.JSON_ELEMENT_TEXT);
                                        String duration = objDuration.getString(Conts.JSON_ELEMENT_TEXT);
                                        duration = duration.replace(Conts.STRING_MINITE_EN, Conts.STRING_MINITE_VN);
                                        onResultTimeDistance.editData(index, distance, duration);
                                    }
                                    onResultTimeDistance.postToHandle();
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }


}
