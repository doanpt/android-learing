package com.ddona.jetpack.model

import android.util.Log
import javax.inject.Inject

class Car @Inject constructor() {
    @Inject
    lateinit var driver: Driver

    fun drive() {
        Log.d("doanpt", "Car is running by driver ${driver.getName()}")
    }
}