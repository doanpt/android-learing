package com.cnc.hcm.cnctrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 1/8/2018.
 */

public class ChangeTicketAppointmentResult {
    @SerializedName("statusCode")
    @Expose
    private long statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
