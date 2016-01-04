package com.dvt.adapters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by doantrung on 10/22/15.
 */
public class ItemImage implements Serializable {
    private String thumbUrl;
    private String titleImage;
    private String linkImageFull;

    public String getLinkImageFull() {
        return linkImageFull;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public ItemImage(JSONObject obj) {
        try {
            this.thumbUrl = obj.getString("tbUrl");
            this.titleImage = obj.getString("title");
            this.linkImageFull = obj.getString("unescapedUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ItemImage() {
    }
}