package com.dvt.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.internal.UnitSystem
import com.dvt.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC//get from setting latter
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
