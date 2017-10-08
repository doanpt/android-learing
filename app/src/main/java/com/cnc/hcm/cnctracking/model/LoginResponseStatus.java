package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

public class LoginResponseStatus {

    public static final String LOGIN_RESPONSE_STATUS_KEY_ACCESS_TOKEN = "accessToken";

    public static final String LOGIN_RESPONSE_STATUS_KEY_MESSAGE = "message";

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private LinkedTreeMap result;

    public LoginResponseStatus() {
    }

    public LoginResponseStatus(Integer statusCode, String message, LinkedTreeMap result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
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

    public LinkedTreeMap getResult() {
        return result;
    }

    public void setResult(LinkedTreeMap result) {
        this.result = result;
    }
}
