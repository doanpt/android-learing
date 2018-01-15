package com.cnc.hcm.cnctracking.model;

public final class GetTaskListResult {
    public final long statusCode;
    public final String message;
    public final GetTaskDetailResult.Result result[];

    public GetTaskListResult(long statusCode, String message, GetTaskDetailResult.Result[] result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}