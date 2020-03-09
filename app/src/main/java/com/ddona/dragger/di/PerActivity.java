package com.ddona.dragger.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

//create scope for per activity
@Scope
@Documented
@Retention(RUNTIME)
public @interface PerActivity {
}
