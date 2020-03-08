package com.ddona.dragger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Engine engine = new Engine();
        Wheels wheels = new Wheels();
        Car car = new Car(engine, wheels);
        car.drive();
    }
}
