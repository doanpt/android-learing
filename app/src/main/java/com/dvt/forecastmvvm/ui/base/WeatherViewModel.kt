package com.dvt.forecastmvvm.ui.base

import androidx.lifecycle.ViewModel
import com.dvt.forecastmvvm.data.provider.UnitProvider
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.internal.UnitSystem
import com.dvt.forecastmvvm.internal.lazyDeferred


abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}