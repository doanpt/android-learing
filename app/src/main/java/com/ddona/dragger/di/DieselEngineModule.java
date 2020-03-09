package com.ddona.dragger.di;

import com.ddona.dragger.model.DieselEngine;
import com.ddona.dragger.model.Engine;


import dagger.Module;
import dagger.Provides;

@Module
public class DieselEngineModule {
    private int horsePower;

    public DieselEngineModule(int horsePower) {
        this.horsePower = horsePower;
    }

    //we add provideHorsePower method to use @Inject annotation on DieselEngine constructor to pass horse power to it
    @Provides
    int provideHorsePower() {
        return horsePower;
    }

    //We can't use binds annotation due to constructor was removed.
    //change to provides annotation to create new Diesel Engine and pass horse power to it.
    @Provides
    Engine provideEngine(DieselEngine engine) {
        return engine;
    }
}
