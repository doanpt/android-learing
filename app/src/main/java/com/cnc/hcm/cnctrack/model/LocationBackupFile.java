package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.util.CommonMethod;

import java.io.Serializable;

/**
 * Created by sev_user on 10/31/2017.
 */

public class LocationBackupFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double latitude;
    private Double longitude;
    private Long timestamp;
    private Float accuracy;
    private boolean isNetwork;

    public LocationBackupFile() {
    }

    @Override
    public String toString() {
        return "{" + "latitude:" + latitude + ", longitude:" + longitude + ", timestamp:" + CommonMethod.formatTimeToString(timestamp) + ", accuracy:" + accuracy + ", isNetwork:" + isNetwork + '}';
    }

    public LocationBackupFile(Double latitude, Double longitude, Long timestamp, Float accuracy, boolean isNetwork) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
        this.isNetwork = isNetwork;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isNetwork() {
        return isNetwork;
    }

    public void setNetwork(boolean network) {
        isNetwork = network;
    }
}
