package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailDevice implements Serializable {

    @SerializedName("startDate")
    @Nullable
    @Expose
    private String startDate;
    @SerializedName("device")
    @Nullable
    @Expose
    private Device device;
    @SerializedName("user")
    @Nullable
    @Expose
    private User user;
    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("status")
    @Nullable
    @Expose
    private Status status;
    @SerializedName("after")
    @Nullable
    @Expose
    private After after;
    @SerializedName("process")
    @Nullable
    @Expose
    private Process process;
    @SerializedName("before")
    @Nullable
    @Expose
    private Before before;

    @SerializedName("endDate")
    @Nullable
    @Expose
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public After getAfter() {
        return after;
    }

    public void setAfter(After after) {
        this.after = after;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Before getBefore() {
        return before;
    }

    public void setBefore(Before before) {
        this.before = before;
    }

    @Nullable
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(@Nullable String endDate) {
        this.endDate = endDate;
    }
}