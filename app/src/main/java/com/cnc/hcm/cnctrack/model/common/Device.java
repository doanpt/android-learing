package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Device implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("detail")
    @Nullable
    @Expose
    private Detail detail;
    @SerializedName("customer")
    @Nullable
    @Expose
    public String customer;
    @SerializedName("__v")
    @Nullable
    @Expose
    public long __v;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    public String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}