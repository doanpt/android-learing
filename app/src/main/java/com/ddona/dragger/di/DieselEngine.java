package com.ddona.dragger.di;

import android.util.Log;

import javax.inject.Inject;

public class DieselEngine implements Engine {
    private static final String TAG = "doanpt";

    //we add this attribute and we need init it at runtime. so we remove constructor injection
    private int horsePower;

    @Inject
    public DieselEngine(int horsePower) {
        this.horsePower = horsePower;
    }

    //We got complie error due to  error: [Dagger/MissingBinding] java.lang.Integer cannot be provided without an @Inject constructor or an @Provides-annotated method
//    @Inject
//    public DieselEngine(int horsePower) {
//        this.horsePower = horsePower;
//    }
    @Override
    public void start() {
        Log.d(TAG, "DieselEngine starting. Horsepower:" + horsePower);
    }
}
