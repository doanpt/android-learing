package com.cnc.hcm.cnctrack.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserInfo {

    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PHONE_NO = "KEY_USER_PHONE_NO";
    private static final String KEY_USER_URL_IMAGE = "KEY_USER_URL_IMAGE";
    private static final String KEY_USER_LOGIN_ON_OTHER_DEVICE = "KEY_USER_LOGIN_ON_OTHER_DEVICE";
    private static final String KEY_MAIN_ACTIVITY_ACTIVE = "KEY_MAIN_ACTIVITY_ACTIVE";

    private static final String KEY_UPLOAD_FIRST_TIME = "KEY_UPLOAD_FIRST_TIME";


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
            try {
                sharedPreferences = context.getSharedPreferences("user_infor", Context.MODE_PRIVATE);
            }catch (Exception e){
                Log.e("doan.pt","Fatal Exception in UserInfo context.getSharedPreferencesuser_infor, Context.MODE_PRIVATE) Line 35");
            }
        }
    }


    public String getAccessToken() {
        if (sharedPreferences != null)
            return sharedPreferences.getString(KEY_ACCESS_TOKEN, Conts.BLANK);
        return Conts.BLANK;
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
        setUserId(Conts.BLANK);
        setUserName(Conts.BLANK);
        setUserEmail(Conts.BLANK);
        setUserPhone(Conts.BLANK);
        setUserUrlImage(Conts.BLANK);
        setUserLoginOnOtherDevice(false);
    }

    private void setLogout() {
        setLogin(false);
    }

    public void setUserId(String id) {
        putString(KEY_USER_ID, id);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, Conts.BLANK);
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

    public void setUploadFirstTime(boolean isUploadFirstTime) {
        putBoolean(KEY_UPLOAD_FIRST_TIME, isUploadFirstTime);
    }

    public boolean getIsUploadFirstTime() {
        return sharedPreferences.getBoolean(KEY_UPLOAD_FIRST_TIME, true);
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