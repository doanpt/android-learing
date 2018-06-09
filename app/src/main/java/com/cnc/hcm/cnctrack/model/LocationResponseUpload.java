package com.cnc.hcm.cnctrack.model;


import com.cnc.hcm.cnctrack.util.CommonMethod;

import java.io.Serializable;

/**
 * Created by Android on 11/17/2017.
 */
//FIXME
//TODO add annotation
public class LocationResponseUpload implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer statusCode;
    private String message;
    private long timespam;

    public LocationResponseUpload() {
    }

    public LocationResponseUpload(Integer statusCode, String message, long timespam) {
        this.statusCode = statusCode;
        this.message = message;
        this.timespam = timespam;
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

    public long getTimespam() {
        return timespam;
    }

    public void setTimespam(long timespam) {
        this.timespam = timespam;
    }

    @Override
    public String toString() {
        return "{" + "statusCode:" + statusCode + ", message:'" + message + ", date:"+ CommonMethod.formatTimeToString(timespam)+'}';
    }
}
