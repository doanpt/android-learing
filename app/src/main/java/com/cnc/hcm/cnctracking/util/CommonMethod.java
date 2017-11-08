package com.cnc.hcm.cnctracking.util;

import com.cnc.hcm.cnctracking.event.OnResultTimeDistance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    public static String format(double number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(number) + Conts.BLANK;
    }

    public static void jsonRequest(double latiOrigin, double longtiOrigin, String destination, final OnResultTimeDistance onResultTimeDistance) {
        if (latiOrigin == 0.0 || longtiOrigin == 0.0 || destination.equals(Conts.BLANK))
            return;
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
                        JSONArray arr = object.getJSONArray(Conts.JSON_ELEMENT_ROWS).getJSONObject(Conts.DEFAULT_VALUE_INT_0).getJSONArray(Conts.JSON_ELEMENT);
                        for (int index = Conts.DEFAULT_VALUE_INT_0; index < arr.length(); index++) {
                            JSONObject objDistance = arr.getJSONObject(index).getJSONObject(Conts.JSON_ELEMENT_DISTANCE);
                            JSONObject objDuration = arr.getJSONObject(index).getJSONObject(Conts.JSON_ELEMENT_DURATION);

                            String distance = objDistance.getString(Conts.JSON_ELEMENT_TEXT);
                            String duration = objDuration.getString(Conts.JSON_ELEMENT_TEXT);
                            duration = duration.replace(Conts.STRING_MINITE_EN, Conts.STRING_MINITE_VN);
                            onResultTimeDistance.editData(index, distance, duration);
                        }
                        onResultTimeDistance.postToHandle();
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
