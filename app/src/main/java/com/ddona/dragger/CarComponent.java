package com.ddona.dragger;

import dagger.Component;

@Component
public interface CarComponent {

    Car getCar();

    //inject object that MainActivity needed
    void inject(MainActivity mainActivity);
}
