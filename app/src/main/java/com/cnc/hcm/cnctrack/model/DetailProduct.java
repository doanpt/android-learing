package com.cnc.hcm.cnctrack.model;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.cnc.hcm.cnctrack.model.common.DetailDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailProduct implements Serializable {

    @SerializedName("statusCode")
    @Nullable
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Nullable
    @Expose
    private String message;
    @SerializedName("result")
    @Nullable
    @Expose
    private DetailDevice result;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetailDevice getResult() {
        return result;
    }

    public void setResult(DetailDevice result) {
        this.result = result;
    }

}