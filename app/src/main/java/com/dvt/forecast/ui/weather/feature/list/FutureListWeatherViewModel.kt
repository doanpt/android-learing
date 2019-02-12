package com.dvt.forecast.ui.weather.feature.list

import com.dvt.forecast.data.provider.UnitProvider
import com.dvt.forecast.data.repository.ForecastRepository
import com.dvt.forecast.internal.lazyDeferred
import com.dvt.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}