package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmChargeResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public ConfirmChargeResponse() {
    }

    /**
     * @param message
     * @param statusCode
     */
    public ConfirmChargeResponse(Integer statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "{" + "statusCode:" + statusCode + ", message:'" + message + '}';
    }
}