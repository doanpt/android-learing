package com.cnc.hcm.cnctrack.model.common;


/**
 * Created by Android on 09/06/2018.
 */

public class Address {
    public String province;
    public String district;
    public String street;
    public Location location;

    public Address(String province, String district, String street, Location location) {
        this.province = province;
        this.district = district;
        this.street = street;
        this.location = location;
    }

    public static class Location {
        public double latitude;
        public double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}

