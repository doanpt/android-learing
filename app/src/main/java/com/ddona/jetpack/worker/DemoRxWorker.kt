package com.ddona.jetpack.worker

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Single

class DemoRxWorker(ctx: Context, params: WorkerParameters) : RxWorker(ctx, params) {
    override fun createWork(): Single<Result> {
        Log.d("doanpt", "work manager run job with RxWorker")
        return Single.just(Result.success())
    }
}