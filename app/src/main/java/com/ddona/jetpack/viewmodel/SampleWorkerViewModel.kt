package com.ddona.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ddona.jetpack.worker.SampleWorker
import java.util.concurrent.TimeUnit

class SampleWorkerViewModel(private val application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)


    fun downloadContent() {
        workManager.enqueue(OneTimeWorkRequest.from(SampleWorker::class.java))
        //If you want to enqueue unique work, do as below:
        //first param is unique name
        //second param is existing work policy: REPLACE,  APPEND, KEEP
        //last param is your request
        //val request = OneTimeWorkRequest.from(SampleWorker::class.java)
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