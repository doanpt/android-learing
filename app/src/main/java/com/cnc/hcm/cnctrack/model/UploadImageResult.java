package com.cnc.hcm.cnctrack.model;

/**
 * Created by Android on 1/18/2018.
 */

import com.cnc.hcm.cnctrack.model.common.ImageURL;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageResult {
    @SerializedName("statusCode")
    @Expose
    private Long statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ImageURL result;

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

    public ImageURL getResult() {
        return result;
    }

    public void setResult(ImageURL result) {
        this.result = result;
    }
}
