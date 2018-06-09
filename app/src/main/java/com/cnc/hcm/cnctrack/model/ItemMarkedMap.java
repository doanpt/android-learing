package com.cnc.hcm.cnctrack.model;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by giapmn on 1/5/18.
 */
//FIXME
//TODO add annotation
public class ItemMarkedMap {
    private String id;
    private Marker marker;

    public ItemMarkedMap(String id, Marker marker) {
        this.id = id;
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
