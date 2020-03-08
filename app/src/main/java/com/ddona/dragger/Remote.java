package com.ddona.dragger;

import android.util.Log;

import javax.inject.Inject;

public class Remote {
    private static final String TAG = "doanpt";

    //@inject annotation let dagger know how to create remote object
    @Inject
    public Remote() {

    }

    public void setListener(Car car) {
        Log.d(TAG, "Remote connected");
    }
}
