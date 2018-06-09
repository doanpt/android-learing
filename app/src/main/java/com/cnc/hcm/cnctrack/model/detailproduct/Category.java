package com.cnc.hcm.cnctrack.model.detailproduct;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Android on 06/06/2018.
 */

public class Category implements Serializable{
    @Expose
    @Nullable
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    @Nullable
    @Expose
    private String title;
    @SerializedName("parent")
    @Expose
    @Nullable
    private String parent;
    @SerializedName("photo")
    @Nullable
    @Expose
    private String photo;
    @SerializedName("createdDate")
    @Nullable
    @Expose
    private String createdDate;
    @SerializedName("description")
    @Nullable
    @Expose
    private String description;
    @SerializedName("__v")
    @Nullable
    @Expose
    private Integer v;
    @SerializedName("active")
    @Nullable
    @Expose
    private Boolean active;

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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
