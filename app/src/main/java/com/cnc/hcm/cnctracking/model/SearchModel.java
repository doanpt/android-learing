package com.cnc.hcm.cnctracking.model;

/**
 * Created by Android on 11/03/2018.
 */

public class SearchModel {
    private String text, category, branch;
    public SearchModel() {
    }

    public SearchModel(String text, String category, String branch) {
        this.text = text;
        this.category = category;
        this.branch = branch;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
