package com.ddona.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ddona.jetpack.network.PassengerApi


@Suppress("UNCHECKED_CAST")
class PassengersViewModelFactory(
    private val api: PassengerApi
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PassengersViewModel(api) as T
    }
}
