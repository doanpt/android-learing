package com.ddona.dragger;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DieselEngineModule {

    @Binds
    //Binds annotation for return implementation for interface type
    abstract Engine bindEngine(DieselEngine engine);
}
