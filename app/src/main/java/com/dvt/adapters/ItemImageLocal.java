package com.dvt.adapters;

import java.io.Serializable;

/**
 * Created by doantrung on 11/2/15.
 */
public class ItemImageLocal implements Serializable{
    private String imageName;
    private String imagePath;

    public ItemImageLocal(String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public ItemImageLocal() {
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
