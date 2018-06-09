package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.TaskDetailResult;

//TODO
//FIXME ADD Annotation
public class GetTaskDetailResult {
    public int statusCode;
    public String message;
    public TaskDetailResult result;

    public GetTaskDetailResult(int statusCode, String message, TaskDetailResult result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

}