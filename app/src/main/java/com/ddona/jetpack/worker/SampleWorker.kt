package com.ddona.jetpack.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class SampleWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    companion object {
        const val TAG = "SampleWorker"
    }

    override fun doWork(): Result {
        return try {
            downloadFileFromNetWork("doanpt.com")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    fun downloadFileFromNetWork(link: String) {
        Log.d(TAG, "We are downloading content from $link")
    }
}