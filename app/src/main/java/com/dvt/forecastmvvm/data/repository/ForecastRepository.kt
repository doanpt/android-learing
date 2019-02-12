package com.dvt.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dvt.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
}