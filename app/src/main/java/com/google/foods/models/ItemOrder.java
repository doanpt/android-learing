package com.google.foods.models;

import java.io.Serializable;

/**
 * Created by sev_user on 08/10/2017.
 */

public class ItemOrder implements Serializable {
    private int orderId;
    private String userName;
    private String userPhone;
    private String userAddress;
    private String foodName;
    private String urlFoodImage;
    private int foodType;
    private int quantityOrder;
    private int totalQuantity;
    private int deliveryType;
    private boolean statusOrder;

    public ItemOrder(int orderId, String userName, String userPhone, String userAddress, String foodName,
                     String urlFoodImage, int foodType, int quantityOrder, int totalQuantity, int deliveryType, boolean statusOrder) {
        this.orderId = orderId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.foodName = foodName;
        this.urlFoodImage = urlFoodImage;
        this.foodType = foodType;
        this.quantityOrder = quantityOrder;
        this.totalQuantity = totalQuantity;
        this.deliveryType = deliveryType;
        this.statusOrder = statusOrder;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getUrlFoodImage() {
        return urlFoodImage;
    }

    public void setUrlFoodImage(String urlFoodImage) {
        this.urlFoodImage = urlFoodImage;
    }

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public boolean isStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(boolean statusOrder) {
        this.statusOrder = statusOrder;
    }

    @Override
    public String toString() {
        return "ItemOrder{" +
                "orderId=" + orderId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", foodName='" + foodName + '\'' +
                ", urlFoodImage='" + urlFoodImage + '\'' +
                ", foodType=" + foodType +
                ", quantityOrder=" + quantityOrder +
                ", totalQuantity=" + totalQuantity +
                ", deliveryType=" + deliveryType +
                ", statusOrder=" + statusOrder +
                '}';
    }
}
