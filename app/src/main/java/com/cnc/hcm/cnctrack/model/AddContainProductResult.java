package com.cnc.hcm.cnctrack.model;

import android.support.annotation.Nullable;

import com.cnc.hcm.cnctrack.model.common.DetailDevice;
import com.cnc.hcm.cnctrack.model.common.Executive;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android on 1/17/2018.
 */

public class AddContainProductResult {
    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("message")
    @Nullable
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Address {

        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("province")
        @Expose
        private String province;
        @SerializedName("location")
        @Expose
        private Location location;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

    }

    public class After {

        @SerializedName("photos")
        @Expose
        private List<String> photos = null;

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public class Before {

        @SerializedName("photos")
        @Expose
        private List<String> photos = null;

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public class Location {

        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

    }

    public class Result {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("service")
        @Expose
        private String service;
        @SerializedName("customer")
        @Expose
        private String customer;
        @SerializedName("note")
        @Expose
        private String note;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("status")
        @Expose
        private Long status;
        @SerializedName("process")
        @Expose
        private List<DetailDevice> process = null;
        @SerializedName("executive")
        @Expose
        private List<Executive> executive = null;
        @SerializedName("appointmentDate")
        @Expose
        private String appointmentDate;
        @SerializedName("address")
        @Expose
        private Address address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public List<DetailDevice> getProcess() {
            return process;
        }

        public void setProcess(List<DetailDevice> process) {
            this.process = process;
        }

        public List<Executive> getExecutive() {
            return executive;
        }

        public void setExecutive(List<Executive> executive) {
            this.executive = executive;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

    }
}