package com.ddona.dragger;

import android.util.Log;

import javax.inject.Inject;

public class Car {
    //Here we have 3 option for inject, Field, constructor and method
    //Order of execution is constructor then field and method
    private static final String TAG = "doanpt";
    //after call inject in constructor then field is inject
    @Inject Engine engine;
    Wheels wheels;

    //Dagger call constructor first
    @Inject
    public Car(Wheels wheels) {
        this.wheels = wheels;
    }

    //final method injection is called
    //after constructor and field injection called finish. dagger will call this method to inject car to remote.
    @Inject
    public void enableRemote(Remote remote) {
        remote.setListener(this);
    }

    void drive() {
        Log.d(TAG, "driving!");
    }
}
