package com.dvt.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dvt.forecastmvvm.data.db.entity.WeatherLocation
import com.dvt.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.dvt.forecastmvvm.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>

    suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
}