package com.cnc.hcm.cnctrack.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09/06/2018.
 */
public class Action {
    @SerializedName("_id")
    @Expose
    public String _id;
    @SerializedName("__v")
    @Expose
    public Integer __v;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;

    public Action(String _id, Integer __v, String title, String createdDate) {
        this._id = _id;
        this.__v = __v;
        this.title = title;
        this.createdDate = createdDate;
    }
}
