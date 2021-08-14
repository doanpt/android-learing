package com.ddona.jetpack.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ddona.jetpack.worker.SampleWorker
import java.time.Duration
import java.util.concurrent.TimeUnit

class SampleWorkerViewModel(private val application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)


    @SuppressLint("IdleBatteryChargingConstraints")
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadContent() {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresDeviceIdle(true)
            .setRequiresStorageNotLow(true)
            .setTriggerContentMaxDelay(Duration.ofMinutes(3))
            .build()
        val request = OneTimeWorkRequest.Builder(SampleWorker::class.java)
            .setConstraints(workConstraints).build()
        workManager.enqueue(request)
        //If you want to enqueue unique work, do as below:
        //first param is unique name
        //second param is existing work policy: REPLACE,  APPEND, KEEP
        //last param is your request
        //workManager.enqueueUniqueWork("UNIQUE_NAME", ExistingWorkPolicy.REPLACE, request)
    }

    fun downloadContentRepeated() {
        val repeatingRequest = PeriodicWorkRequestBuilder<SampleWorker>(1, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "UNIQUE_PERIODIC_NAME",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}