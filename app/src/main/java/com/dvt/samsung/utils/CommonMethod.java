package com.dvt.samsung.utils;

/**
 * Created by Android on 11/4/2016.
 */

public class CommonMethod {
    private static CommonMethod commonMethod;

    public static CommonMethod getInstance() {
        if (commonMethod == null) {
            commonMethod = new CommonMethod();
        }
        return commonMethod;
    }
    
}

