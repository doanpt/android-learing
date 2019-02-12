package com.dvt.forecastmvvm.ui.weather.feature.list

import androidx.lifecycle.ViewModel;
import com.dvt.forecastmvvm.data.provider.UnitProvider
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.internal.lazyDeferred
import com.dvt.forecastmvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}