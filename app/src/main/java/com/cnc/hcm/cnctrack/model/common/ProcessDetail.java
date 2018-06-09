package com.cnc.hcm.cnctrack.model.common;


/**
 * Created by Android on 09/06/2018.
 */

public class ProcessDetail {
    public Device_Services services[];
    public Device_Products products[];
    public String[] photos;

    public ProcessDetail(Device_Services[] services, Device_Products[] products, String[] photos) {
        this.services = services;
        this.products = products;
        this.photos = photos;
    }
}
