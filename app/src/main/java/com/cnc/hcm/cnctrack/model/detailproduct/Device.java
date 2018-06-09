package com.cnc.hcm.cnctrack.model.detailproduct;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Device  implements Serializable {

    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("detail")
    @Nullable
    @Expose
    private Detail detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}