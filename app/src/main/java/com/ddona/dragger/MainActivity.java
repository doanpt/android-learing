package com.ddona.dragger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    //dagger is not support private field
    @Inject Car car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CarComponent carComponent = DaggerCarComponent.create();
        //add this to let dagger know we need inject car to this project
        //if we don't do it. car object will be null although we have @inject annotation on Car object
        carComponent.inject(this);
        //We don't need it and we use field injection to inject car to main activity
        //car = carComponent.getCar();
        car.drive();
    }
}
