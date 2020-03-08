package com.ddona.dragger;

import dagger.Component;

@Component(modules = {WheelsModule.class})
public interface CarComponent {

    Car getCar();

    //inject object that MainActivity needed
    //we need more inject method if there are many activities or fragments
    void inject(MainActivity mainActivity);
}
