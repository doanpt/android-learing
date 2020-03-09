package com.ddona.dragger.di;

import com.ddona.dragger.model.Driver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DriverModule.class})
public interface AppComponent {

    Driver getDiver();
}
