package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("fullname")
    @Nullable
    @Expose
    private String fullname;
    @SerializedName("email")
    @Nullable
    @Expose
    private String email;
    @SerializedName("phone")
    @Nullable
    @Expose
    private String phone;
    @SerializedName("position")
    @Nullable
    @Expose
    private Integer position;
    @SerializedName("skill")
    @Nullable
    @Expose
    private Integer skill;
    @SerializedName("photo")
    @Nullable
    @Expose
    private String photo;
    @SerializedName("dateOfBirth")
    @Nullable
    @Expose
    private String dateOfBirth;
    @SerializedName("address")
    @Nullable
    @Expose
    private String address;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    private String createdDate;
    @SerializedName("__v")
    @Nullable
    @Expose
    private long __v;
    @SerializedName("group")
    @Nullable
    @Expose
    private long group;
    @SerializedName("isDriver")
    @Nullable
    @Expose
    private boolean isDriver;
    @SerializedName("agent")
    @Nullable
    @Expose
    private String agent;

    @Nullable
    public long get__v() {
        return __v;
    }

    public void set__v(@Nullable long __v) {
        this.__v = __v;
    }

    @Nullable
    public long getGroup() {
        return group;
    }

    public void setGroup(@Nullable long group) {
        this.group = group;
    }

    @Nullable
    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(@Nullable boolean driver) {
        isDriver = driver;
    }

    @Nullable
    public String getAgent() {
        return agent;
    }

    public void setAgent(@Nullable String agent) {
        this.agent = agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}