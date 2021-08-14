package com.ddona.jetpack.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ddona.jetpack.util.Const
import com.ddona.jetpack.worker.SampleWorker
import java.time.Duration
import java.util.concurrent.TimeUnit

class SampleWorkerViewModel(private val application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    private val sampleWorkInfo: MutableList<WorkInfo> =
        workManager.getWorkInfosByTag(Const.SAMPLE_WORK_TAG).get()
    val wasSuccess = sampleWorkInfo[0].outputData.getString("is_success")
//    val sampleWorkInfo = workManager.getWorkInfoById("")


    //This method will be pass a link to download worker
    @SuppressLint("RestrictedApi")
    fun setInputForWork(link: String): Data {
        //create data using workDataOf from ktx
        val workData = workDataOf("param1" to "value 1", "param2" to "value 2")
        val builder = Data.Builder()
        builder.put(Const.DOWNLOAD_URL, link)
        builder.putAll(workData)
        return builder.build()
    }

    @SuppressLint("IdleBatteryChargingConstraints")
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadContent(link: String) {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresDeviceIdle(true)
            .setRequiresStorageNotLow(true)
            .setTriggerContentMaxDelay(Duration.ofMinutes(3))
            .build()
        val request = OneTimeWorkRequest.Builder(SampleWorker::class.java)
            .setConstraints(workConstraints)
            .setInputData(setInputForWork(link))
            .addTag(Const.SAMPLE_WORK_TAG)
            .build()
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