package com.ddona.dragger.model;

import com.ddona.dragger.di.DieselEngine;
import com.ddona.dragger.di.Engine;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DieselEngineModule {

    @Binds
    //Binds annotation for return implementation for interface type
    abstract Engine bindEngine(DieselEngine engine);
}
