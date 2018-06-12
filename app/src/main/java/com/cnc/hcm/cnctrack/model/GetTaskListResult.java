package com.cnc.hcm.cnctrack.model;


import com.cnc.hcm.cnctrack.model.common.TaskListResult;

//FIXME
//TODO add annotation
public class GetTaskListResult {
    public long statusCode;
    public String message;
    public TaskListResult[] result;

    public GetTaskListResult(long statusCode, String message, TaskListResult[] result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}