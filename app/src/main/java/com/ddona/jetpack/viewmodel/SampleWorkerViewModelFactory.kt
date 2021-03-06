package com.ddona.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SampleWorkerViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SampleWorkerViewModel::class.java)) {
            return SampleWorkerViewModel(application) as T
        }
        throw NotImplementedError()
    }
}