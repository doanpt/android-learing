package com.cnc.hcm.cnctrack.model.common;


import com.cnc.hcm.cnctrack.model.GetTaskDetailResult;

/**
 * Created by Android on 09/06/2018.
 */

public class TaskDetail {
    public long __v;
    public String _id;
    public String title;
    public Service service;
    public Customer customer;
    public String note;
    public String createdDate;
    public GetTaskDetailResult.Result.Recipient recipient;
    public boolean isRead;
    public GetTaskDetailResult.Result.Invoice invoice;
    public GetTaskDetailResult.Result.Status status;
    public GetTaskDetailResult.Result.Process process[];
    public GetTaskDetailResult.Result.Executive executive[];
    public String appointmentDate;
    public GetTaskDetailResult.Result.Address address;
    public GetTaskDetailResult.Result.RecommendedServices[] recommendedServices;
}
