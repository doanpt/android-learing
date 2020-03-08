package com.ddona.dragger;

import android.util.Log;

import javax.inject.Inject;

public class Car {
    Engine engine;
    Wheels wheels;

    @Inject
    public Car(Engine engine, Wheels wheels) {
        this.engine = engine;
        this.wheels = wheels;
    }

    void drive() {
        Log.d("doanpt", "driving!");
    }
}
