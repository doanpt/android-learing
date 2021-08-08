package com.ddona.jetpack.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddona.jetpack.util.SingleLiveEvent

class CountingViewModel(application: Application) : ViewModel() {
    var count = MutableLiveData<Int>()
    var message = MutableLiveData<String>()
    init {
        count.postValue(0)
        // We need application instance to create RoomDatabase
    }

    private val _navigateToDetails = SingleLiveEvent<Any>()

    val navigateToDetails : LiveData<Any>
        get() = _navigateToDetails

    fun userClicksOnButton() {
        _navigateToDetails.value = true
    }

    fun increaseValue() {
        count.postValue(count.value?.plus(1))
    }

    fun makeToast(view: View) {
        Log.d("doanpt", "your message is: ${message.value}")
    }
}