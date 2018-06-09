package com.cnc.hcm.cnctrack.model.common;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09/06/2018.
 */
public class Address {
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("district")
    @Expose
    private District district;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

