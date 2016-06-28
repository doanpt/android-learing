package com.dvt.adapter;

/**
 * Created by Doanp on 6/24/2016.
 */

public class ExamScheduleItem {
    String tenMon,ngayThi,caThi,soBaoDanh,phongThi;

    public ExamScheduleItem(String tenMon, String ngayThi, String caThi, String soBaoDanh, String phongThi) {
        this.tenMon = tenMon;
        this.ngayThi = ngayThi;
        this.caThi = caThi;
        this.soBaoDanh = soBaoDanh;
        this.phongThi = phongThi;
    }

    public ExamScheduleItem() {
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(String ngayThi) {
        this.ngayThi = ngayThi;
    }

    public String getCaThi() {
        return caThi;
    }

    public void setCaThi(String caThi) {
        this.caThi = caThi;
    }

    public String getSoBaoDanh() {
        return soBaoDanh;
    }

    public void setSoBaoDanh(String soBaoDanh) {
        this.soBaoDanh = soBaoDanh;
    }

    public String getPhongThi() {
        return phongThi;
    }

    public void setPhongThi(String phongThi) {
        this.phongThi = phongThi;
    }
}
