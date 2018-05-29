
package com.cnc.hcm.cnctrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserGroup {

    @SerializedName("_id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private Integer role;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserGroup() {
    }

    /**
     * 
     * @param id
     * @param name
     * @param role
     */
    public UserGroup(Integer id, String name, Integer role) {
        super();
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}
