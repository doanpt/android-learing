package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Detail implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("brand")
    @Nullable
    @Expose
    private Brand brand;
    @SerializedName("category")
    @Nullable
    @Expose
    private Category category;
    @SerializedName("name")
    @Nullable
    @Expose
    private String name;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    private String createdDate;
    @SerializedName("__v")
    @Nullable
    @Expose
    public long __v;
    @SerializedName("photo")
    @Nullable
    @Expose
    public String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}