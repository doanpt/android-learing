package com.cnc.hcm.cnctrack.model.detailproduct;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Unit implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("title")
    @Nullable
    @Expose
    private String title;
    @SerializedName("active")
    @Nullable
    @Expose
    private Boolean active;
    @SerializedName("description")
    @Nullable
    @Expose
    private String description;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

