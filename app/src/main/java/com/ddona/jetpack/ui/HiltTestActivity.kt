package com.ddona.jetpack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddona.jetpack.R
import com.ddona.jetpack.di.HeaderInterceptorOkHttpClient
import com.ddona.jetpack.di.LoggingInterceptorOkHttpClient
import com.ddona.jetpack.model.Car
import com.ddona.jetpack.network.AnalyticsService
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import javax.inject.Inject

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    //Note that Hilt does not support inject private field
    @Inject
    lateinit var car: Car

    @Inject
    lateinit var analyticsService: AnalyticsService

    @LoggingInterceptorOkHttpClient
    @Inject lateinit var okHttpClientForLogging: OkHttpClient

    @HeaderInterceptorOkHttpClient
    @Inject lateinit var okHttpClientForHeader: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)
        car.drive()
        analyticsService.analyticsMethods()
    }
}