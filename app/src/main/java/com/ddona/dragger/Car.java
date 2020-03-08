package com.ddona.dragger;

import android.util.Log;

public class Car {
    Engine engine;
    Wheels wheels;

    public Car(Engine engine, Wheels wheels) {
        this.engine = engine;
        this.wheels = wheels;
    }

    void drive() {
        Log.d("doanpt", "driving!");
    }
}
