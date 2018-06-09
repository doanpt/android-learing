package com.cnc.hcm.cnctrack.model.detailproduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Android on 08/06/2018.
 */

public class ResultDeviceContain {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
