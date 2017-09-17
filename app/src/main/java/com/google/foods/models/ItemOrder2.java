package com.google.foods.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by sev_user on 08/10/2017.
 */

public class ItemOrder2 implements Serializable, Comparable<ItemOrder2> {

    private String orderId;
    private String userName;
    private String userPhone;
    private String userAddress;
    private int deliveryType;
    private int totalPrice;
    private int comboPrice;
    private int priceNeedPay;
    private Date dateTime;
    private int statusOrder;
    private List<ItemFood> foodList;

    public ItemOrder2() {
    }

    public ItemOrder2(String orderId, String userName, String userPhone, String userAddress,
                      int deliveryType, int totalPrice, int comboPrice, int priceNeedPay,
                      Date dateTime, int statusOrder, List<ItemFood> foodList) {
        this.orderId = orderId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.deliveryType = deliveryType;
        this.totalPrice = totalPrice;
        this.comboPrice = comboPrice;
        this.priceNeedPay = priceNeedPay;
        this.dateTime = dateTime;
        this.statusOrder = statusOrder;
        this.foodList = foodList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getComboPrice() {
        return comboPrice;
    }

    public void setComboPrice(int comboPrice) {
        this.comboPrice = comboPrice;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(int statusOrder) {
        this.statusOrder = statusOrder;
    }

    public List<ItemFood> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<ItemFood> foodList) {
        this.foodList = foodList;
    }

    public int getPriceNeedPay() {
        return priceNeedPay;
    }

    public void setPriceNeedPay(int priceNeedPay) {
        this.priceNeedPay = priceNeedPay;
    }

    @Override
    public int compareTo(@NonNull ItemOrder2 o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
