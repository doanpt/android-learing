package com.cnc.hcm.cnctracking.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 11/7/17.
 */

public class ItemTask {

    private GetTaskListResult.Result taskResult;
    private String distanceToMyLocation;
    private String timeGoToMyLocation;

    public ItemTask(GetTaskListResult.Result taskResult) {
        this.taskResult = taskResult;
        distanceToMyLocation = "0 km";
        timeGoToMyLocation = Conts.BLANK;
    }

    public GetTaskListResult.Result getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(GetTaskListResult.Result taskResult) {
        this.taskResult = taskResult;
    }

    public String getDistanceToMyLocation() {
        return distanceToMyLocation;
    }

    public void setDistanceToMyLocation(String distanceToMyLocation) {
        this.distanceToMyLocation = distanceToMyLocation;
    }

    public String getTimeGoToMyLocation() {
        return timeGoToMyLocation;
    }

    public void setTimeGoToMyLocation(String timeGoToMyLocation) {
        this.timeGoToMyLocation = timeGoToMyLocation;
    }
}