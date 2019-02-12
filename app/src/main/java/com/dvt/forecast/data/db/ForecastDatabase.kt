package com.resocoder.forecastmvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dvt.forecast.data.db.CurrentWeatherDao
import com.dvt.forecast.data.db.FutureWeatherDao
import com.dvt.forecast.data.db.LocalDateConverter
import com.dvt.forecast.data.db.WeatherLocationDao
import com.dvt.forecast.data.db.entity.CurrentWeatherEntry
import com.dvt.forecast.data.db.entity.FutureWeatherEntry
import com.dvt.forecast.data.db.entity.WeatherLocation


@Database(
    entities = [CurrentWeatherEntry::class, FutureWeatherEntry::class, WeatherLocation::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java, "forecast.db"
            ).build()
    }
}