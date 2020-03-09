package com.ddona.dragger.di;

import com.ddona.dragger.model.Rims;
import com.ddona.dragger.model.Tires;
import com.ddona.dragger.model.Wheels;

import dagger.Module;
import dagger.Provides;

@Module
//we will module annotation for object that we can't use @Inject in constructor for that class.
//Due to we don't own of that classes.
//or we have any configuration before return class for dagger
//If all method of module is static. we can add abstract key word to avoid dagger create instance of module
public abstract class WheelsModule {

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
