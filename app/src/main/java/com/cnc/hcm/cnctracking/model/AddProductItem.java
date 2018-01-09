package com.cnc.hcm.cnctracking.model;

/**
 * Created by Android on 1/9/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductItem {

    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("customer")
    @Expose
    private String customer;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public static class Detail {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("category")
        @Expose
        private String category;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

    }
}