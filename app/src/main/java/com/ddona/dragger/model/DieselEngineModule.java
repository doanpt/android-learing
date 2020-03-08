package com.ddona.dragger.model;

import com.ddona.dragger.di.DieselEngine;
import com.ddona.dragger.di.Engine;


import dagger.Module;
import dagger.Provides;

@Module
public class DieselEngineModule {
    private int horsePower;

    public DieselEngineModule(int horsePower) {
        this.horsePower = horsePower;
    }

    //We can't use binds annotation due to constructor was removed.
    //change to provides annotation to create new Diesel Engine and pass horse power to it.
    @Provides
    Engine provideEngine() {
        return new DieselEngine(horsePower);
    }
}
