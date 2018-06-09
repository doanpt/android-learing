package com.cnc.hcm.cnctrack.model;

/**
 * Created by Android on 1/3/2018.
 */

import com.cnc.hcm.cnctrack.model.common.ResultDeviceContain;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckContainProductResult {

    @SerializedName("statusCode")
    @Expose
    private Long statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ResultDeviceContain result;

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultDeviceContain getResult() {
        return result;
    }

    public void setResult(ResultDeviceContain result) {
        this.result = result;
    }

}
