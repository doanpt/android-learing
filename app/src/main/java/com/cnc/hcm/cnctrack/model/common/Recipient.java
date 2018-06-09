package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */

public class Recipient {
    private String fullname;
    private String phone;

    public Recipient(String fullname, String phone) {
        this.fullname = fullname;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }
}
