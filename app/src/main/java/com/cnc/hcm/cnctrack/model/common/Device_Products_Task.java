package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Device_Products_Task implements Serializable {

    @SerializedName("product")
    @Nullable
    @Expose
    private ProductTaskListResult product;
    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("quantity")
    @Nullable
    @Expose
    private Integer quantity;

    public ProductTaskListResult getProduct() {
        return product;
    }

    public void setProduct(ProductTaskListResult product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}

