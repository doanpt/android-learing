package com.ddona.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountingViewModel(application: Application) : ViewModel() {
    var count = MutableLiveData<Int>()

    init {
        count.postValue(0)
        // We need application instance to create RoomDatabase
    }

    fun increaseValue() {
        count.postValue(count.value?.plus(1))
    }
}