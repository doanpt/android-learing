package com.ddona.dragger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ddona.dragger.di.ActivityComponent;
import com.ddona.dragger.di.DaggerActivityComponent;
import com.ddona.dragger.model.Car;

import javax.inject.Inject;

//code from tut: https://www.youtube.com/playlist?list=PLrnPJCHvNZuA2ioi4soDZKz8euUQnJW65
//read more about scope and dager
//https://android.jlelse.eu/dagger-2-part-i-basic-principles-graph-dependencies-scopes-3dfd032ccd82
//https://proandroiddev.com/dagger-2-part-ii-custom-scopes-component-dependencies-subcomponents-697c1fa1cfc
//https://proandroiddev.com/dagger-2-part-three-new-possibilities-3daff12f7ebf
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
        ActivityComponent carComponent = DaggerActivityComponent.builder()
                .horsePower(150)
                .engineCapacity(1400)
                .appComponent(((DaggerTutApplication)getApplication()).getAppComponent())
                .build();
        //add this to let dagger know we need inject car to this project
        //if we don't do it. car object will be null although we have @inject annotation on Car object
        carComponent.inject(this);
        //We don't need it and we use field injection to inject car to main activity
        //car = carComponent.getCar();
        //for scope annotation, there are only driver even we rotate device.. due to we use singleton for driver class and app component
        //but when we rotate device. we get new car due to we use @PerActivity Scope.
        car1.drive();
        car2.drive();
    }
}
