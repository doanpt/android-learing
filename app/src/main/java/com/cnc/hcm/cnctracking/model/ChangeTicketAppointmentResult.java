package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 1/8/2018.
 */

public class ChangeTicketAppointmentResult {
    @SerializedName("statusCode")
    @Expose
    private Long statusCode;
    @SerializedName("message")
    @Expose
    private String message;

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
}
