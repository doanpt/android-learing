
package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("skill")
    @Expose
    private Integer skill;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("group")
    @Expose
    private UserGroup group;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("isDriver")
    @Expose
    private Boolean isDriver;
    @SerializedName("agent")
    @Expose
    private String agent;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserProfile() {
    }

    /**
     * 
     * @param isDriver
     * @param position
     * @param id
     * @param phone
     * @param accessToken
     * @param skill
     * @param email
     * @param group
     * @param agent
     * @param photo
     * @param fullname
     */
    public UserProfile(String id, String fullname, String email, String phone, Integer position, Integer skill, String accessToken, UserGroup group, String photo, Boolean isDriver, String agent) {
        super();
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.skill = skill;
        this.accessToken = accessToken;
        this.group = group;
        this.photo = photo;
        this.isDriver = isDriver;
        this.agent = agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getIsDriver() {
        return isDriver;
    }

    public void setIsDriver(Boolean isDriver) {
        this.isDriver = isDriver;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

}
