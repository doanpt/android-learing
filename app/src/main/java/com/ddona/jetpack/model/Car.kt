package com.ddona.jetpack.model

import android.util.Log
import javax.inject.Inject

class Car @Inject constructor() {
    fun drive() {
        Log.d("doanpt", "Car is running")
    }
}