package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable{

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("name")
    @Nullable
    @Expose
    private String name;
    @SerializedName("price")
    @Nullable
    @Expose
    private Integer price;
    @SerializedName("brand")
    @Nullable
    @Expose
    private Brand brand;
    @SerializedName("category")
    @Nullable
    @Expose
    private Category category;
    @SerializedName("tax")
    @Nullable
    @Expose
    private Integer tax;
    @SerializedName("unit")
    @Nullable
    @Expose
    private Unit unit;
    @SerializedName("quantity")
    @Nullable
    @Expose
    private Integer quantity;
    @SerializedName("photo")
    @Nullable
    @Expose
    private String photo;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    private String createdDate;
    @SerializedName("__v")
    @Nullable
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}