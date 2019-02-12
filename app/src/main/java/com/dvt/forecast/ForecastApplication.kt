package com.dvt.forecast

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.dvt.forecast.data.network.ApixuWeatherApiService
import com.dvt.forecast.data.network.ConnectivityInterceptorImpl
import com.dvt.forecast.data.network.WeatherNetworkDataSource
import com.dvt.forecast.data.network.WeatherNetworkDataSourceImpl
import com.dvt.forecast.data.provider.LocationProvider
import com.dvt.forecast.data.provider.LocationProviderImpl
import com.dvt.forecast.data.provider.UnitProvider
import com.dvt.forecast.data.provider.UnitProviderImpl
import com.dvt.forecast.data.repository.ForecastRepository
import com.dvt.forecast.data.repository.ForecastRepositoryImpl
import com.dvt.forecast.ui.weather.current.CurrentWeatherViewModelFactory
import com.dvt.forecast.ui.weather.feature.detail.FutureDetailWeatherViewModelFactory
import com.dvt.forecast.ui.weather.feature.list.FutureListWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.resocoder.forecastmvvm.data.db.ForecastDatabase
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FutureListWeatherViewModelFactory(instance(), instance()) }
        bind() from factory { detailDate: LocalDate -> FutureDetailWeatherViewModelFactory(detailDate, instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}