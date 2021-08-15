package com.ddona.jetpack

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors

@HiltAndroidApp
class JetpackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(
            applicationContext,
            Configuration.Builder().setExecutor(Executors.newFixedThreadPool(8))
                .build()
        )
    }
}