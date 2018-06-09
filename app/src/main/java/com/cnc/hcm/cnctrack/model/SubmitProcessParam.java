package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.After;
import com.cnc.hcm.cnctrack.model.common.Before;
import com.cnc.hcm.cnctrack.model.common.ProcessSubmitItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 1/21/2018.
 */

public class SubmitProcessParam {
    @SerializedName("before")
    @Expose
    private Before before;
    @SerializedName("process")
    @Expose
    private ProcessSubmitItem process;
    @SerializedName("after")
    @Expose
    private After after;

    public Before getBefore() {
        return before;
    }

    public void setBefore(Before before) {
        this.before = before;
    }

    public ProcessSubmitItem getProcess() {
        return process;
    }

    public void setProcess(ProcessSubmitItem process) {
        this.process = process;
    }

    public After getAfter() {
        return after;
    }

    public void setAfter(After after) {
        this.after = after;
    }

}
