package com.dvt.samsung.model;

/**
 * Created by sev_user on 11/7/2016.
 */

public class TypeItem {
    private int id;
    private String nameType;
    private String number;

    public TypeItem(int id, String nameType, String number) {
        this.id = id;
        this.nameType = nameType;
        this.number = number;
    }

    public TypeItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
