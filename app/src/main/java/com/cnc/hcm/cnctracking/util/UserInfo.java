package com.cnc.hcm.cnctracking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserInfo {

    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PHONE_NO = "KEY_USER_PHONE_NO";
    private static final String KEY_USER_URL_IMAGE = "KEY_USER_URL_IMAGE";
    private static final String KEY_USER_LOGIN_ON_OTHER_DEVICE = "KEY_USER_LOGIN_ON_OTHER_DEVICE";
    private static final String KEY_MAIN_ACTIVITY_ACTIVE = "KEY_MAIN_ACTIVITY_ACTIVE";


    private static UserInfo userInfo;

    private SharedPreferences sharedPreferences;

    public static UserInfo getInstance(Context context) {
        if (userInfo == null) {
            userInfo = new UserInfo(context);
        }
        return userInfo;
    }

    private UserInfo(Context context) {
        if (context != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }


    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, Conts.BLANK);
    }

    private void setAccessToken(String accessToken) {
        putString(KEY_ACCESS_TOKEN, accessToken);
    }

    public void setUserInfoLogin(String accessToken) {
        setLogin(true);
        setAccessToken(accessToken);
    }

    private void setLogin(boolean isLogin) {
        putBoolean(KEY_IS_LOGIN, isLogin);
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(KEY_IS_LOGIN, false);
    }

    public void setUserInfoLogout() {
        setLogout();
        setAccessToken(Conts.BLANK);
        setUserName(Conts.BLANK);
        setUserEmail(Conts.BLANK);
        setUserPhone(Conts.BLANK);
        setUserUrlImage(Conts.BLANK);
        setUserLoginOnOtherDevice(false);
    }

    private void setLogout() {
        setLogin(false);
    }


    public void setUserName(String name) {
        putString(KEY_USER_NAME, name);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USER_NAME, Conts.BLANK);
    }

    public void setUserEmail(String email) {
        putString(KEY_USER_EMAIL, email);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, Conts.BLANK);
    }

    public void setUserPhone(String phoneNo) {
        putString(KEY_USER_PHONE_NO, phoneNo);
    }

    public String getUserPhoneNo() {
        return sharedPreferences.getString(KEY_USER_PHONE_NO, Conts.BLANK);
    }

    public void setUserUrlImage(String urlImage) {
        putString(KEY_USER_URL_IMAGE, urlImage);
    }

    public String getUserUrlImage() {
        return sharedPreferences.getString(KEY_USER_URL_IMAGE, Conts.BLANK);
    }

    public void setUserLoginOnOtherDevice(boolean isLoginOnOtherDevice) {
        putBoolean(KEY_USER_LOGIN_ON_OTHER_DEVICE, isLoginOnOtherDevice);
    }

    public boolean getUserLoginOnOtherDevice() {
        return sharedPreferences.getBoolean(KEY_USER_LOGIN_ON_OTHER_DEVICE, false);
    }

    public void setMainActivityActive(boolean isActive) {
        putBoolean(KEY_MAIN_ACTIVITY_ACTIVE, isActive);
    }

    public boolean getMainActivityActive() {
        return sharedPreferences.getBoolean(KEY_MAIN_ACTIVITY_ACTIVE, true);
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