package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.TaskDetailResult;
//FIXME
//TODO add annotation
public class GetTaskListResult {
    public long statusCode;
    public String message;
    public TaskDetailResult[] result;

    public GetTaskListResult(long statusCode, String message, TaskDetailResult[] result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}