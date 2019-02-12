package com.dvt.forecast.ui.weather.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvt.forecast.data.provider.UnitProvider
import com.dvt.forecast.data.repository.ForecastRepository


class FutureListWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(
            forecastRepository,
            unitProvider
        ) as T
    }
}