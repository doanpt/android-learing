package com.cnc.hcm.cnctracking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by giapmn on 12/22/17.
 */

public class SettingApp {

    private static final String KEY_FILTER_LIST = "KEY_FILTER_LIST";
    private static final String KEY_TYPE_VIEW_BY_MONTH_OR_YEAR = "KEY_TYPE_VIEW_BY_MONTH_OR_YEAR";


    private static SettingApp settingApp;
    private SharedPreferences sharedPreferences;

    public static SettingApp getInstance(Context context) {
        if (settingApp == null) {
            settingApp = new SettingApp(context);
        }
        return settingApp;
    }

    private SettingApp(Context context) {
        if (context != null) {
            sharedPreferences = context.getSharedPreferences("setting_app", Context.MODE_PRIVATE);
        }
    }


    public void setTypeFilterList(int type) {
        putInt(KEY_FILTER_LIST, type);
    }

    public int getTypeFilterList() {
        return sharedPreferences.getInt(KEY_FILTER_LIST, Conts.TYPE_ALL_TASK);
    }

    public void setTypeView(int type) {
        putInt(KEY_TYPE_VIEW_BY_MONTH_OR_YEAR, type);
    }

    public int getTypeView() {
        return sharedPreferences.getInt(KEY_TYPE_VIEW_BY_MONTH_OR_YEAR, Conts.TYPE_VIEW_BY_MONTH);
    }

    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
