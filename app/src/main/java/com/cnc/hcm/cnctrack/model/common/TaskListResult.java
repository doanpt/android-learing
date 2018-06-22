package com.cnc.hcm.cnctrack.model.common;


import android.support.annotation.Nullable;

/**
 * Created by Android on 09/06/2018.
 */

public class TaskListResult {
    public long __v;
    public String _id;
    public String title;
    @Nullable
    public Customer customer;
    public String note;
    public String createdDate;
    public Recipient recipient;
    public boolean isRead;
    public InvoiceTaskDetail invoice;
    public Status status;
    public DetailDevice process[];
    public Executive executive[];
    public String appointmentDate;
    public Address address;
    public RecommendedServices recommendedServices[];

    public long get__v() {
        return __v;
    }

    public void set__v(long __v) {
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public InvoiceTaskDetail getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceTaskDetail invoice) {
        this.invoice = invoice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DetailDevice[] getProcess() {
        return process;
    }

    public void setProcess(DetailDevice[] process) {
        this.process = process;
    }

    public Executive[] getExecutive() {
        return executive;
    }

    public void setExecutive(Executive[] executive) {
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

    public RecommendedServices[] getRecommendedServices() {
        return recommendedServices;
    }

    public void setRecommendedServices(RecommendedServices[] recommendedServices) {
        this.recommendedServices = recommendedServices;
    }
}
