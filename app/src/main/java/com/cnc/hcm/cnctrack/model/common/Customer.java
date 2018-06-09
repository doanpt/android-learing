package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */

public class Customer {
    public String _id;
    public String type;
    public String fullname;
    public String phone;
    public String email;
    public String gender;
    public String dateOfBirth;
    public long __v;
    public String createdDate;
    public Address address;

    public Customer(String _id, String type, String fullname, String phone, String email, String gender, String dateOfBirth, long __v, String createdDate, Address address) {
        this._id = _id;
        this.type = type;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.__v = __v;
        this.createdDate = createdDate;
        this.address = address;
    }
}
