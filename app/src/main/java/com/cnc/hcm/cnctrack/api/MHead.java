package com.cnc.hcm.cnctrack.api;

/**
 * Created by Android on 1/8/2018.
 */

public class MHead {
    private String key;
    private String value;

    public MHead() {
    }

    public MHead(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
