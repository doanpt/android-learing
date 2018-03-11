package com.cnc.hcm.cnctracking.model;

/**
 * Created by Android on 11/03/2018.
 */

public class SearchServiceModel {
    private String name, category;

    public SearchServiceModel() {
    }

    public SearchServiceModel(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
