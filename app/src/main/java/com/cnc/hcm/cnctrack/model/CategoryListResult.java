package com.cnc.hcm.cnctrack.model;

/**
 * Created by Android on 1/9/2018.
 */

import com.cnc.hcm.cnctrack.model.common.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResult {

    @SerializedName("statusCode")
    @Expose
    private Long statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Category> result = null;

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

    public List<Category> getResult() {
        return result;
    }

    public void setResult(List<Category> result) {
        this.result = result;
    }

}