package com.cnc.hcm.cnctracking.model;

import com.cnc.hcm.cnctracking.util.CommonMethod;

import java.io.Serializable;

/**
 * Created by sev_user on 10/31/2017.
 */

public class LocationUploadSize implements Serializable {
    private static final long serialVersionUID = 1L;
    private String size;
    private long time;
    private String account;
    public LocationUploadSize(String size, long time,String account) {
        this.size = size;
        this.time = time;
        this.account=account;
    }

    public LocationUploadSize() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" + "size:" + size + ", time=:" + CommonMethod.formatTimeToString(time) + ", account=:" + account + '}';
    }
}
