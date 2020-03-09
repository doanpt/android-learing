package com.ddona.dragger.model;

import android.util.Log;

import com.ddona.dragger.di.PerActivity;

import javax.inject.Inject;

@PerActivity
public class Car {
    //Here we have 3 option for inject, Field, constructor and method
    //Order of execution is constructor then field and method
    private static final String TAG = "doanpt";
    //Back to constructor injection code
    private Engine engine;
    private Wheels wheels;
    private Driver driver;

    //Dagger call constructor first
    @Inject
    public Car(Driver driver, Engine engine, Wheels wheels) {
        this.driver = driver;
        this.engine = engine;
        this.wheels = wheels;
    }

    //final method injection is called
    //after constructor and field injection called finish. dagger will call this method to inject car to remote.
    @Inject
    public void enableRemote(Remote remote) {
        remote.setListener(this);
    }

    public void drive() {
        engine.start();
        Log.d(TAG, "driver : " + driver + " is driving!" + this);
    }
}
