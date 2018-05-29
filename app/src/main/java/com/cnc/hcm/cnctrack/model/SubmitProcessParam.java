package com.cnc.hcm.cnctrack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 1/21/2018.
 */

public class SubmitProcessParam {
    @SerializedName("before")
    @Expose
    private Before before;
    @SerializedName("process")
    @Expose
    private Process process;
    @SerializedName("after")
    @Expose
    private After after;

    public Before getBefore() {
        return before;
    }

    public void setBefore(Before before) {
        this.before = before;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public After getAfter() {
        return after;
    }

    public void setAfter(After after) {
        this.after = after;
    }


    public static class Before {

        @SerializedName("photos")
        @Expose
        private List<String> photos = new ArrayList<>();

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public static class After {

        @SerializedName("photos")
        @Expose
        private List<String> photos = new ArrayList<>();

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public static class Process {

        @SerializedName("photos")
        @Expose
        private List<String> photos = new ArrayList<>();
        @SerializedName("products")
        @Expose
        private List<Product> products = new ArrayList<>();
        @SerializedName("services")
        @Expose
        private List<Service> services = new ArrayList<>();
        @SerializedName("note")
        @Expose
        private String note;

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class Product {

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

    public static class Service {

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
}
