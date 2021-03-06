package com.ddona.jetpack.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ddona.jetpack.util.Const
import java.lang.Exception

class SampleWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    companion object {
        const val TAG = "SampleWorker"
    }

    override fun doWork(): Result {
        return try {
            val downloadUrl = inputData.getString(Const.DOWNLOAD_URL)
            val param1 = inputData.getString("param1")
            val param2 = inputData.getString("param2")
            Log.d(TAG, "input data is $downloadUrl and $param1 and $param2")
            downloadFileFromNetWork(downloadUrl!!)
            val outputData = workDataOf("is_success" to true)
            Result.success(outputData)
        } catch (e: Exception) {
            val outputData = workDataOf("is_success" to false)
            Result.failure(outputData)
        }
    }

    fun downloadFileFromNetWork(link: String) {
        Log.d(TAG, "We are downloading content from $link")
    }
}