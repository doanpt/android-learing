package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */
//FIXME
//TODO add annotation
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

    public RService getService() {
        return service;
    }

    public void setService(RService service) {
        this.service = service;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
