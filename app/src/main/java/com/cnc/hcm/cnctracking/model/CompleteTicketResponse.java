package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteTicketResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;

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

    public CompleteTicketResponse() {
    }

    public CompleteTicketResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}