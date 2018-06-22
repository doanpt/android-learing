package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private Integer id;
    @SerializedName("title")
    @NonNull
    @Expose
    private String title;
    @SerializedName("description")
    @Nullable
    @Expose
    private String description;
    @SerializedName("icon")
    @Nullable
    @Expose
    private String icon;
    @SerializedName("mobileIcon")
    @Nullable
    @Expose
    private String mobileIcon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    public void setIcon(@Nullable String icon) {
        this.icon = icon;
    }

    @Nullable
    public String getMobileIcon() {
        return mobileIcon;
    }

    public void setMobileIcon(@Nullable String mobileIcon) {
        this.mobileIcon = mobileIcon;
    }
}