package com.ddona.dragger.model;

import com.ddona.dragger.MainActivity;
import com.ddona.dragger.di.Car;

import dagger.Component;

@Component(modules = {WheelsModule.class, DieselEngineModule.class})
//we can't add PetrolEngineModule and DieselEngineModule into Car component together due to Engine is bound multiple times error when build
//@Component(modules = {WheelsModule.class, PetrolEngineModule.class,DieselEngineModule.class})
public interface CarComponent {

    Car getCar();

    //inject object that MainActivity needed
    //we need more inject method if there are many activities or fragments
    void inject(MainActivity mainActivity);
}
