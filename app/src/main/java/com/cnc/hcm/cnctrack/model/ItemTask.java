package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.TaskDetailResult;
import com.cnc.hcm.cnctrack.util.Conts;

/**
 * Created by giapmn on 11/7/17.
 */
//FIXME
//TODO add annotation
public class ItemTask {

    private TaskDetailResult taskResult;
    private String distanceToMyLocation;
    private String timeGoToMyLocation;

    public ItemTask(TaskDetailResult taskResult) {
        this.taskResult = taskResult;
        distanceToMyLocation = "0 km";
        timeGoToMyLocation = Conts.BLANK;
    }

    public TaskDetailResult getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(TaskDetailResult taskResult) {
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
