package com.cnc.hcm.cnctrack.model.common;

import com.cnc.hcm.cnctrack.model.SubmitProcessParam;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 09/06/2018.
 */

public class ProcessSubmitItem {
    @SerializedName("photos")
    @Expose
    private List<String> photos = new ArrayList<>();
    @SerializedName("products")
    @Expose
    private List<SubmitProductItem> products = new ArrayList<>();
    @SerializedName("services")
    @Expose
    private List<SubmitServiceItem> services = new ArrayList<>();
    @SerializedName("note")
    @Expose
    private String note;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<SubmitProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<SubmitProductItem> products) {
        this.products = products;
    }

    public List<SubmitServiceItem> getServices() {
        return services;
    }

    public void setServices(List<SubmitServiceItem> services) {
        this.services = services;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
