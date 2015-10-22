package com.dvt.adapters;

/**
 * Created by doantrung on 10/22/15.
 */
public class ItemImage {
    private String thumbUrl;
    private String titleImage;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public ItemImage(String thumbUrl, String titleImage) {
        this.thumbUrl = thumbUrl;
        this.titleImage = titleImage;
    }

    public ItemImage() {
    }
}
