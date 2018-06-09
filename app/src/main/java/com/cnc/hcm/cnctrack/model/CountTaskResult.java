package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.CountTask;
import com.cnc.hcm.cnctrack.model.common.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by giapmn on 1/15/18.
 */

public class CountTaskResult {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<CountTask> result = null;

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

    public List<CountTask> getResult() {
        return result;
    }

    public void setResult(List<CountTask> result) {
        this.result = result;
    }


}
