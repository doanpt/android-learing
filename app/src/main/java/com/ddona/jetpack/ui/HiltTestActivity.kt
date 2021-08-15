package com.ddona.jetpack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddona.jetpack.R
import com.ddona.jetpack.model.Car
import com.ddona.jetpack.network.AnalyticsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    //Note that Hilt does not support inject private field
    @Inject
    lateinit var car: Car

    @Inject
    lateinit var analyticsService: AnalyticsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)
        car.drive()
        analyticsService.analyticsMethods()
    }
}