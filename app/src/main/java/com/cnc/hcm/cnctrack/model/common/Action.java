package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */
//FIXME
//TODO add annotation    
public class Action {
    public String _id;
    public Integer __v;
    public String title;
    public String createdDate;

    public Action(String _id, Integer __v, String title, String createdDate) {
        this._id = _id;
        this.__v = __v;
        this.title = title;
        this.createdDate = createdDate;
    }
}
