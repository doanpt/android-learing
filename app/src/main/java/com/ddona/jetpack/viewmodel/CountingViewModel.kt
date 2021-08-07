package com.ddona.jetpack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountingViewModel : ViewModel() {
    var count = MutableLiveData<Int>()

    init {
        count.postValue(0)
    }

    fun increaseValue() {
        count.postValue(count.value?.plus(1))
    }
}