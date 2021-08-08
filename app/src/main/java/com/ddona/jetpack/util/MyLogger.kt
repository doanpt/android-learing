package com.ddona.jetpack.util

import android.util.Log

class MyLogger {
    companion object {
        private const val TAG = "MyLogger"
    }

    fun logCreate() {
        Log.d(TAG, "Activity created")
    }

    fun logStart() {
        Log.d(TAG, "Activity started")
    }

    fun logResume() {
        Log.d(TAG, "Activity resumed")
    }

    fun logPause() {
        Log.d(TAG, "Activity will pause")
    }

    fun logStop() {
        Log.d(TAG, "Activity will stop")
    }

    fun logDestroy() {
        Log.d(TAG, "Activity will be destroyed")
    }
}