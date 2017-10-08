
package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserProfileResponseStatus {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private UserProfile result;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetUserProfileResponseStatus() {
    }

    /**
     * 
     * @param message
     * @param statusCode
     * @param result
     */
    public GetUserProfileResponseStatus(Integer statusCode, String message, UserProfile result) {
        super();
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

    public UserProfile getResult() {
        return result;
    }

    public void setResult(UserProfile result) {
        this.result = result;
    }

}
