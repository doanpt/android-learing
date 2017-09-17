package com.google.foods.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by giapmn on 8/17/17.
 */

public class UserInfor {

    private static final String FILE_SAVE_USER_INFOR = "setting_user_infor";
    private static final String KEY_USER_LOGIN = "KEY_USER_LOGIN";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_ADDRESS = "KEY_USER_ADDRESS";
    private static final String KEY_USER_PHONE_NUMBER = "KEY_USER_PHONE_NUMBER";

    private static final String KEY_ADMIN_LOGIN = "KEY_ADMIN_LOGIN";
    private static final String KEY_USER_NAME_ADMIN = "KEY_USER_NAME_ADMIN";



    private static UserInfor userInfor;
    private Context context;

    public static UserInfor getInstance(Context context) {
        if (userInfor == null) {
            userInfor = new UserInfor(context);
        }
        return userInfor;
    }

    private UserInfor(Context context) {
        this.context = context;
    }

    public String getUserName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, CommonValue.BLANK);
    }

    private void setUserName(String userName) {
        putValue(KEY_USER_NAME, userName);
    }

    public String getAddress() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ADDRESS, CommonValue.BLANK);
    }

    private void setAddress(String address) {
        putValue(KEY_USER_ADDRESS, address);
    }

    public String getPhoneNumber() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE_NUMBER, CommonValue.BLANK);
    }

    private void setPhoneNumber(String phoneNumber) {
        putValue(KEY_USER_PHONE_NUMBER, phoneNumber);
    }


    private void putValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void setAdminUsername(String usernameAdmin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME_ADMIN, usernameAdmin);
        editor.commit();
    }

    public String getAdminUsername(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME_ADMIN, CommonValue.BLANK);
    }

    private void setAdminLogin(boolean isLogin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ADMIN_LOGIN, isLogin);
        editor.commit();
    }

    public boolean isAdminLogin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_ADMIN_LOGIN, false);
    }

    public boolean isUserLogin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_USER_LOGIN, false);
    }

    private void setUserLogin(boolean isUserLogin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SAVE_USER_INFOR, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_LOGIN, isUserLogin);
        editor.commit();

    }



    public void setInforAdminLogin( String adminName){
        setAdminLogin(true);
        setAdminUsername(adminName);
    }

    public void setAminLogout(){
        setAdminUsername(CommonValue.BLANK);
        setAdminLogin(false);
    }

    public void setInforUserLogin(String username, String address, String phoneNo){
        setUserLogin(true);
        setUserName(username);
        setAddress(address);
        setPhoneNumber(phoneNo);
    }

    public void setUserLogout(){
        setUserLogin(false);
        setUserName(CommonValue.BLANK);
        setAddress(CommonValue.BLANK);
        setPhoneNumber(CommonValue.BLANK);
    }


}
