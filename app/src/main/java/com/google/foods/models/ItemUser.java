package com.google.foods.models;

import java.io.Serializable;

/**
 * Created by giapmn on 8/16/17.
 */

public class ItemUser implements Serializable {

    String userName;
    String address;
    String dateBirthday;
    String phoneNo;
    String password;
    boolean isAdmin;


    public ItemUser() {

    }

    public ItemUser(String userName, String address, String dateBirthday, String phoneNo, String password, boolean isAdmin) {
        this.userName = userName;
        this.address = address;
        this.dateBirthday = dateBirthday;
        this.phoneNo = phoneNo;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(String dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isisAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
