package com.dvt.forecast.ui.weather.current

import com.dvt.forecast.data.provider.UnitProvider
import com.dvt.forecast.data.repository.ForecastRepository
import com.dvt.forecast.internal.lazyDeferred
import com.dvt.forecast.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }
}