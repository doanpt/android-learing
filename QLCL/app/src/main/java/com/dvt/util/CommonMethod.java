package com.dvt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by DoanPT1 on 6/29/2016.
 */
public class CommonMethod {
    private CommonMethod() {

    }

    private static CommonMethod commonMethod = null;

    public static CommonMethod getInstance() {
        if (commonMethod == null) {
            commonMethod = new CommonMethod();
        }
        return commonMethod;
    }

    public String getFile(Context context, String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filename), "UTF-8"));
            String mLine;
            StringBuilder sb = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.d("ErrorFile", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static String getCode(Context context) {
        String code = "";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mCode = preferences.getString(CommonValue.RESULT_CODE_STUDENT, "");
        if (!mCode.equalsIgnoreCase("")) {
            code = mCode;
        }
        return code;
    }

    public static void setCode(Context context, String code) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CommonValue.RESULT_CODE_STUDENT, code);
        editor.apply();
    }

    public boolean isValid(CharSequence s) {
        return CommonValue.sPattern.matcher(s).matches();
    }

}
