package com.ddona.dragger.model;

import com.ddona.dragger.di.Rims;
import com.ddona.dragger.di.Tires;
import com.ddona.dragger.di.Wheels;

import dagger.Module;
import dagger.Provides;

@Module
//we will module annotation for object that we can't use @Inject in constructor for that class.
//Due to we don't own of that classes.
//or we have any configuration before return class for dagger
public class WheelsModule {

    //provide Rims object with @Provides annotation
    //naming convention is provide<Object name>
    @Provides
    static Rims provideRims() {
        return new Rims();
    }

    @Provides
    static Tires provideTires() {
        Tires tires = new Tires();
        //we can create new tires object and call some method for any configuration before return it
        tires.inflate();
        return tires;
    }

    @Provides
    static Wheels provideWheels(Rims rims, Tires tires) {
        //dagger know how to to create rims and tires, so dagger will create it and pass it as param of provideWheels method
        return new Wheels(rims, tires);
    }
}
