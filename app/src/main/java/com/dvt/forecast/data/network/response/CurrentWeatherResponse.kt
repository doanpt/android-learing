package com.dvt.forecast.data.network.response

import com.dvt.forecast.data.db.entity.CurrentWeatherEntry
import com.dvt.forecast.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)