package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */
//FIXME
//TODO add annotation
public class Type {
    public final String _id;
    public final long __v;
    public final String title;
    public final String createdDate;

    public Type(String _id, long __v, String title, String createdDate) {
        this._id = _id;
        this.__v = __v;
        this.title = title;
        this.createdDate = createdDate;
    }
}
