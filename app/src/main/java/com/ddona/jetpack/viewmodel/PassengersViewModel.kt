package com.ddona.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ddona.jetpack.network.PassengerApi
import com.ddona.jetpack.network.PassengersDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassengersViewModel @Inject constructor(
    private val passengerApi: PassengerApi
) : ViewModel() {
    val passengers = Pager(PagingConfig(pageSize = 10)) {
        PassengersDataSource(passengerApi)
    }.flow.cachedIn(viewModelScope)
}