package com.ddona.jetpack.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class ChainWorkB(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        Log.d("doanpt", "Chain B starting")
        return Result.success(workDataOf("status" to "Work B Done"))
    }
}