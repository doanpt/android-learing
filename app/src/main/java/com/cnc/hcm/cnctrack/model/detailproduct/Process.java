package com.cnc.hcm.cnctrack.model.detailproduct;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Process  implements Serializable {

    @SerializedName("note")
    @Nullable
    @Expose
    private String note;
    @SerializedName("services")
    @Nullable
    @Expose
    private List<Device_Services> services = null;
    @SerializedName("products")
    @Nullable
    @Expose
    private List<Device_Products> products = null;
    @SerializedName("photos")
    @Nullable
    @Expose
    private List<String> photos = null;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Device_Services> getServices() {
        return services;
    }

    public void setServices(List<Device_Services> services) {
        this.services = services;
    }

    public List<Device_Products> getProducts() {
        return products;
    }

    public void setProducts(List<Device_Products> products) {
        this.products = products;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

}
