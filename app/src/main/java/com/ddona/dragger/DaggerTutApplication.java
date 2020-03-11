package com.ddona.dragger;

import android.app.Application;

import com.ddona.dragger.di.AppComponent;
import com.ddona.dragger.di.DaggerAppComponent;
import com.ddona.dragger.di.DriverModule;

public class DaggerTutApplication extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.factory().create(new DriverModule("Doan"));
    }

    public AppComponent getAppComponent() {
        return component;
    }
}
