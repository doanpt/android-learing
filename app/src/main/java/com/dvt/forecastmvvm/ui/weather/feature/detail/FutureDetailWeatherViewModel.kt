package com.dvt.forecastmvvm.ui.weather.feature.detail

import com.dvt.forecastmvvm.data.provider.UnitProvider
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.internal.lazyDeferred
import com.dvt.forecastmvvm.ui.base.WeatherViewModel
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