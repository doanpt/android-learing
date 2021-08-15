package com.ddona.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Due to we are using Hilt, we don't need to create Factory for ViewModel

//class TaskViewModelFactory(private val application: Application) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
//            return TaskViewModel(application) as T
//        }
//        throw NotImplementedError()
//    }
//}