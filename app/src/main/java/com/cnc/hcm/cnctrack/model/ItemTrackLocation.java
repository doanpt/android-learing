
package com.cnc.hcm.cnctrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemTrackLocation {

    @SerializedName("trackLocation")
    @Expose
    private List<TrackLocation> trackLocation = null;

    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;

    public ItemTrackLocation() {
    }

    public ItemTrackLocation(List<TrackLocation> trackLocation, Integer batteryLevel) {
        this.trackLocation = trackLocation;
        this.batteryLevel = batteryLevel;
    }

    public List<TrackLocation> getTrackLocation() {
        return trackLocation;
    }

    public void setTrackLocation(List<TrackLocation> trackLocation) {
        this.trackLocation = trackLocation;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

}
