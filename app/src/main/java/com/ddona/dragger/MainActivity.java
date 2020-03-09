package com.ddona.dragger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ddona.dragger.model.Car;
import com.ddona.dragger.di.CarComponent;
import com.ddona.dragger.di.DaggerCarComponent;

import javax.inject.Inject;

//code from tut: https://www.youtube.com/playlist?list=PLrnPJCHvNZuA2ioi4soDZKz8euUQnJW65
public class MainActivity extends AppCompatActivity {
    //dagger is not support private field
    @Inject
    Car car1;
    @Inject
    Car car2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //We use builder to add module for dagger component
        //when we use  @Component.Builder then dagger don't create dieselEngineModule method for add builder
        //and we can add methods to dagger builder like horsePower.
        //Note: we need rebuild project.
        CarComponent carComponent = DaggerCarComponent.builder()
                .horsePower(150)
                .engineCapacity(1400)
                .build();
        //add this to let dagger know we need inject car to this project
        //if we don't do it. car object will be null although we have @inject annotation on Car object
        carComponent.inject(this);
        //We don't need it and we use field injection to inject car to main activity
        //car = carComponent.getCar();
        car1.drive();
        car2.drive();


        //If you create 2 component then cars will be drive by different driver.
        //Singleton only work when you get car on same car component
        //if you want to use same car component in many class. you must create car component in Application class once time.
//        CarComponent carComponent1 = DaggerCarComponent.builder()
//                .horsePower(130)
//                .engineCapacity(1400)
//                .build();
//        CarComponent carComponent2 = DaggerCarComponent.builder()
//                .horsePower(140)
//                .engineCapacity(1400)
//                .build();
//        carComponent1.getCar().drive();
//        carComponent2.getCar().drive();
    }
}
