package com.cnc.hcm.cnctrack.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09/06/2018.
 */

public class SubmitProductItem {
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("quantity")
    @Expose
    private Long quantity;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
