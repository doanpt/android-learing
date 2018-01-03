package com.cnc.hcm.cnctracking.model.adddevice;

/**
 * Created by Android on 1/3/2018.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("process")
    @Expose
    private List<Object> process = null;
    @SerializedName("executive")
    @Expose
    private List<Object> executive = null;
    @SerializedName("appointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("address")
    @Expose
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Object> getProcess() {
        return process;
    }

    public void setProcess(List<Object> process) {
        this.process = process;
    }

    public List<Object> getExecutive() {
        return executive;
    }

    public void setExecutive(List<Object> executive) {
        this.executive = executive;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}