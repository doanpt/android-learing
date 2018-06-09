package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */

public class RecommendedServices {
    public RService service;
    public String device;
    public String _id;
    public boolean isDefault;
    public long quantity;

    public RecommendedServices(RService service, String device, String _id, boolean isDefault, long quantity) {
        this.service = service;
        this.device = device;
        this._id = _id;
        this.isDefault = isDefault;
        this.quantity = quantity;
    }
}
