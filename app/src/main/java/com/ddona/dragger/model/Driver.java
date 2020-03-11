package com.ddona.dragger.model;


public class Driver {
    //We don't own this class so we can't annotate it with @Inject
    String name;

    public Driver(String name) {
        this.name = name;
    }
}
