package com.cnc.hcm.cnctrack.model.detailproduct;

/**
 * Created by Android on 06/06/2018.
 */

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Before  implements Serializable {

    @SerializedName("photos")
    @Nullable
    @Expose
    private List<String> photos = null;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

}
