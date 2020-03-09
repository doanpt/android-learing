package com.ddona.dragger.di;

import com.ddona.dragger.MainActivity;
import com.ddona.dragger.model.Car;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {WheelsModule.class, PetrolEngineModule.class})
//we can't add PetrolEngineModule and DieselEngineModule into Car component together due to Engine is bound multiple times error when build
//@Component(modules = {WheelsModule.class, PetrolEngineModule.class,DieselEngineModule.class})
public interface CarComponent {

    Car getCar();

    //inject object that MainActivity needed
    //we need more inject method if there are many activities or fragments
    void inject(MainActivity mainActivity);

    //@Component.Builder annotation to create custom component builder when build dagger component.
    //@BindInstance: add attribute value to instance at runtime same as pass horse value to module at runtime and providing it via provide method like last video,
    //and it more efficient due to dagger doesn't need to create an instance of module(PetrolEngineModule)
    //@Named: Use to name for some attribute that have same type to let dagger know where are the values will be inject to?
    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder horsePower(@Named("horse power") int horsePower);

        @BindsInstance
        Builder engineCapacity(@Named("engine capacity") int engineCapacity);

        //add method for dagger create builder for CarComponent
        CarComponent build();
    }
}
