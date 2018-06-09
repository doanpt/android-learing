package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */
//FIXME
//TODO ADD annotation
public class Invoice {
    public String modifiedDate;
    public String createdDate;
    public Status status;
    public int discount;
    public Device_Services[] services;
    public Device_Products[] products;

    public Invoice(String modifiedDate, String createdDate, Status status, int discount, Device_Services[] services, Device_Products[] products) {
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
        this.status = status;
        this.discount = discount;
        this.services = services;
        this.products = products;
    }
}
