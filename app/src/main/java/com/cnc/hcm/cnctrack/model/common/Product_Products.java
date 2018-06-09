package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */


import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_Products  implements Serializable {

    @SerializedName("statusCode")
    @Nullable
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Nullable
    @Expose
    private String message;
    @SerializedName("meta")
    @Nullable
    @Expose
    private Meta meta;
    @SerializedName("result")
    @Nullable
    @Expose
    private List<Product> listProduct = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

}