package com.cnc.hcm.cnctracking.model;

/**
 * Created by giapmn on 11/7/17.
 */

public class ItemWork {

    private int typeWork;
    private String timeGetWork;
    private String titleWork;
    private double latitude;
    private double longitude;
    private String distanceToMyLocation;
    private String timeGoToMyLocation;
    private String scheduleWork;
    private String contact;
    private String phoneNo;
    private String address;
    private String requestService;
    private String price;
    private String noteService;
    private boolean statusWork;
    private String timeStart;
    private String timeEnd;
    private String totalTimeToCompleted;
    private String totalMoney;
    private boolean statusPayment;

    private boolean isExpand;


    public ItemWork(int typeWork, String timeGetWork, String titleWork, double latitude, double longitude,
                    String distanceToMyLocation, String timeGoToMyLocation, String scheduleWork, String contactWork, String phoneNo, String address,
                    String requestService, String price, String noteService, boolean statusWork,
                    String timeStart, String timeEnd, String totalTimeToCompleted, String totalMoney,
                    boolean statusPayment) {
        this.typeWork = typeWork;
        this.timeGetWork = timeGetWork;
        this.titleWork = titleWork;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distanceToMyLocation = distanceToMyLocation;
        this.timeGoToMyLocation = timeGoToMyLocation;
        this.scheduleWork = scheduleWork;
        this.contact = contactWork;
        this.phoneNo = phoneNo;
        this.address = address;
        this.requestService = requestService;
        this.price = price;
        this.noteService = noteService;
        this.statusWork = statusWork;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.totalTimeToCompleted = totalTimeToCompleted;
        this.totalMoney = totalMoney;
        this.statusPayment = statusPayment;
        this.isExpand = false;
    }

    public int getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(int typeWork) {
        this.typeWork = typeWork;
    }

    public String getTimeGetWork() {
        return timeGetWork;
    }

    public void setTimeGetWork(String timeGetWork) {
        this.timeGetWork = timeGetWork;
    }

    public String getTitleWork() {
        return titleWork;
    }

    public void setTitleWork(String titleWork) {
        this.titleWork = titleWork;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getScheduleWork() {
        return scheduleWork;
    }

    public void setScheduleWork(String scheduleWork) {
        this.scheduleWork = scheduleWork;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequestService() {
        return requestService;
    }

    public void setRequestService(String requestService) {
        this.requestService = requestService;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNoteService() {
        return noteService;
    }

    public void setNoteService(String noteService) {
        this.noteService = noteService;
    }

    public boolean isStatusWork() {
        return statusWork;
    }

    public void setStatusWork(boolean statusWork) {
        this.statusWork = statusWork;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTotalTimeToCompleted() {
        return totalTimeToCompleted;
    }

    public void setTotalTimeToCompleted(String totalTimeToCompleted) {
        this.totalTimeToCompleted = totalTimeToCompleted;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public boolean isStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(boolean statusPayment) {
        this.statusPayment = statusPayment;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getDistanceToMyLocation() {
        return distanceToMyLocation;
    }

    public void setDistanceToMyLocation(String distanceToMyLocation) {
        this.distanceToMyLocation = distanceToMyLocation;
    }

    public String getTimeGoToMyLocation() {
        return timeGoToMyLocation;
    }

    public void setTimeGoToMyLocation(String timeGoToMyLocation) {
        this.timeGoToMyLocation = timeGoToMyLocation;
    }
}
