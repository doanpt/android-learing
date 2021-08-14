package com.ddona.jetpack.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ddona.jetpack.util.Const
import com.ddona.jetpack.worker.*
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class SampleWorkerViewModel(private val application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    val status = MutableLiveData<Boolean>()
    var sampleWorkStatus = workManager.getWorkInfosByTagLiveData(Const.SAMPLE_WORK_TAG)

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
//            .setRequiresBatteryNotLow(true)
//            .setRequiresCharging(true)
//            .setRequiresDeviceIdle(true)
//            .setRequiresStorageNotLow(true)
//            .setTriggerContentMaxDelay(Duration.ofMinutes(3))
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

    fun startChainWork() {
        val workA = OneTimeWorkRequest.from(ChainWorkA::class.java)
        val workB = OneTimeWorkRequest.from(ChainWorkB::class.java)
        val workC = OneTimeWorkRequest.from(ChainWorkC::class.java)
        val workD = OneTimeWorkRequest.from(ChainWorkD::class.java)
        workManager.beginWith(workA)
            //Note: WorkManager beginWith() return a WorkContinuation object
            //the following calls are to WorkContinuation methods
            .then(workB)// then() return a new WorkContinuation instance
            .then(workC)
            .then(workD)
            .enqueue()
    }

    fun startChainWorkParallel() {
        val workA = OneTimeWorkRequest.from(ChainWorkA::class.java)
        val workB = OneTimeWorkRequest.from(ChainWorkB::class.java)
        val workC = OneTimeWorkRequest.from(ChainWorkC::class.java)
        val workD = OneTimeWorkRequest.from(ChainWorkD::class.java)
        val workE = OneTimeWorkRequest.from(ChainWorkE::class.java)
        workManager.beginWith(listOf(workC, workD))
            .then(workA)
            .then(listOf(workB, workE))
            .enqueue()
    }

    @SuppressLint("EnqueueWork")
    fun startComplexChainWork() {
        val workA = OneTimeWorkRequest.from(ChainWorkA::class.java)
        val workB = OneTimeWorkRequest.from(ChainWorkB::class.java)
        val workC = OneTimeWorkRequest.from(ChainWorkC::class.java)
        val workD = OneTimeWorkRequest.from(ChainWorkD::class.java)
        val workE = OneTimeWorkRequest.from(ChainWorkD::class.java)
        val chain1 = workManager
            .beginWith(workA)
            .then(workD)
        val chain2 = workManager.beginWith(listOf(workE, workB))
        //WorkE will wail for starting until chain1 and chain2 done
        val chain3 = WorkContinuation.combine(listOf(chain1, chain2)).then(workC)
        chain3.enqueue()
    }

    fun startRxWorker() {
        workManager.enqueue(OneTimeWorkRequest.from(DemoRxWorker::class.java))
    }

    fun startCoroutineWorker() {
        workManager.enqueue(OneTimeWorkRequest.from(DemoCoroutineWork::class.java))
    }

}