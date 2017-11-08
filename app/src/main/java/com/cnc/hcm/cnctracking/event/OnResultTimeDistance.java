package com.cnc.hcm.cnctracking.event;

/**
 * Created by giapmn on 11/8/17.
 */

public interface OnResultTimeDistance {
    void editData(int index, String distance, String duration);
    void postToHandle();
}
