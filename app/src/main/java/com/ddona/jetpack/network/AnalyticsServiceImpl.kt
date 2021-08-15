package com.ddona.jetpack.network

import android.util.Log
import javax.inject.Inject

class AnalyticsServiceImpl @Inject constructor() : AnalyticsService {

    override fun analyticsMethods() {
        Log.d("doanpt", "This is implement of AnalyticsService");
    }
}