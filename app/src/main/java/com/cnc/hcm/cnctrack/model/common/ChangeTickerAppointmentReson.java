package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */
//FIXME
//TODO add annotation    
public class ChangeTickerAppointmentReson {
    public String _id;
    public String reason;
    public Action action;
    public Integer __v;
    public String createdDate;

    public ChangeTickerAppointmentReson(String _id, String reason, Action action, Integer __v, String createdDate) {
        this._id = _id;
        this.reason = reason;
        this.action = action;
        this.__v = __v;
        this.createdDate = createdDate;
    }
}
