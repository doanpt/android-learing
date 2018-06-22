package com.cnc.hcm.cnctrack.model.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09/06/2018.
 */
public class Customer {
    @SerializedName("_id")
    @NonNull
    @Expose
    public String _id;
    @SerializedName("type")
    @NonNull
    @Expose
    public String type;
    @SerializedName("fullname")
    @NonNull
    @Expose
    public String fullname;
    @SerializedName("phone")
    @NonNull
    @Expose
    public String phone;
    @SerializedName("email")
    @NonNull
    @Expose
    public String email;
    @SerializedName("gender")
    @Nullable
    @Expose
    public String gender;
    @SerializedName("dateOfBirth")
    @Nullable
    @Expose
    public String dateOfBirth;
    @SerializedName("__v")
    @Nullable
    @Expose
    public long __v;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    public String createdDate;
    @SerializedName("address")
    @Nullable
    @Expose
    public Address address;

    public Customer(String _id, String type, String fullname, String phone, String email, String gender, String dateOfBirth, long __v, String createdDate, Address address) {
        this._id = _id;
        this.type = type;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.__v = __v;
        this.createdDate = createdDate;
        this.address = address;
    }
}
