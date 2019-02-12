package com.dvt.forecast.ui.weather.feature.detail

import com.dvt.forecast.data.provider.UnitProvider
import com.dvt.forecast.data.repository.ForecastRepository
import com.dvt.forecast.internal.lazyDeferred
import com.dvt.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
        private val detailDate: LocalDate,
        private val forecastRepository: ForecastRepository,
        unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}