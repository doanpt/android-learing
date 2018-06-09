package com.cnc.hcm.cnctrack.model.common;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09/06/2018.
 */

public class Executive {
    @SerializedName("user")
    @Nullable
    @Expose
    private User user;
    @SerializedName("_id")
    @Nullable
    @Expose
    private String id;
    @SerializedName("joinedDate")
    @Nullable
    @Expose
    private String joinedDate;
    @SerializedName("isLeader")
    @Nullable
    @Expose
    private Boolean isLeader;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Boolean isLeader) {
        this.isLeader = isLeader;
    }
}
