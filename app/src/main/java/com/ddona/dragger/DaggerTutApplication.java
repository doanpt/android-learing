package com.ddona.dragger;

import android.app.Application;

import com.ddona.dragger.di.AppComponent;
import com.ddona.dragger.di.DaggerAppComponent;

public class DaggerTutApplication extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return component;
    }
}
