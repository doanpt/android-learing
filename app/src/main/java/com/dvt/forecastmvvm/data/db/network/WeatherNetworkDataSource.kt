package com.dvt.forecastmvvm.data.db.network

import androidx.lifecycle.LiveData
import com.dvt.forecastmvvm.data.db.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}