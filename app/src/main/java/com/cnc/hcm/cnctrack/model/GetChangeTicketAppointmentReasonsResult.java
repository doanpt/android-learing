package com.cnc.hcm.cnctrack.model;

import java.util.List;

public class GetChangeTicketAppointmentReasonsResult {

    public Integer statusCode;
    public String message;
    public List<Result> result;

    public GetChangeTicketAppointmentReasonsResult(Integer statusCode, String message, List<Result> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static final class Result {
        public String _id;
        public String reason;
        public Action action;
        public Integer __v;
        public String createdDate;

        public Result(String _id, String reason, Action action, Integer __v, String createdDate) {
            this._id = _id;
            this.reason = reason;
            this.action = action;
            this.__v = __v;
            this.createdDate = createdDate;
        }

        public static final class Action {
            public String _id;
            public Integer __v;
            public String title;
            public String createdDate;

            public Action(String _id, Integer __v, String title, String createdDate) {
                this._id = _id;
                this.__v = __v;
                this.title = title;
                this.createdDate = createdDate;
            }
        }
    }
}
