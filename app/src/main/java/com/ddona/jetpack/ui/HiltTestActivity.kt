package com.ddona.jetpack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddona.jetpack.R
import com.ddona.jetpack.model.Car

class HiltTestActivity: AppCompatActivity() {
    private lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)
        car = Car()
        car.drive()
    }
}