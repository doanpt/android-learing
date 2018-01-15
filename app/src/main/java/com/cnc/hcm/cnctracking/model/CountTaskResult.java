package com.cnc.hcm.cnctracking.model;

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
    private List<Result> result = null;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("date")
        @Expose
        private Date date;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }


        public class Date {

            @SerializedName("year")
            @Expose
            private Integer year;
            @SerializedName("month")
            @Expose
            private Integer month;
            @SerializedName("day")
            @Expose
            private Integer day;

            public Integer getYear() {
                return year;
            }

            public void setYear(Integer year) {
                this.year = year;
            }

            public Integer getMonth() {
                return month;
            }

            public void setMonth(Integer month) {
                this.month = month;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

        }
    }
}
