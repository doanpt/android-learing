package com.ddona.dragger;

public class Car {
    Engine engine;
    Wheels wheels;

    public Car() {
        //this is problems, car don't create it's engine and wheels
        engine = new Engine();
        wheels = new Wheels();
    }

    void drive() {

    }
}
