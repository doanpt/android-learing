package com.ddona.dragger;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PetrolEngineModule {

//    @Provides
//    Engine provideEngine(PetrolEngine engine) {
//        return engine;
//    }
    //Bind is same with provide method above, but it is less code
    //if you want return a implementation class as Interface then you should use @Bind annotation
    //Binds method only have a parameter is implementation of interface
    @Binds
    abstract Engine bindEngine(PetrolEngine engine);

    //we can't use provide method in this abstract due to provide need create instance of module.
    //if you create provide, you need to add static in first of method like below
//    @Provides
//    static Engine provideEngine(PetrolEngine engine) {
//        return engine;
//    }
}
