package com.dvt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
    public String readFromFile(String filename,Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    public void clearTheFile(String filename) {
        FileWriter fwOb = null;
        try {
            fwOb = new FileWriter(filename, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToFile(String data,String filename,Context context) {
        try {
            clearTheFile(filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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
