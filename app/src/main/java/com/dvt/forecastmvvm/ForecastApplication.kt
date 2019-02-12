package com.dvt.forecastmvvm

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.dvt.forecastmvvm.data.network.ApixuWeatherApiService
import com.dvt.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.dvt.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dvt.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.dvt.forecastmvvm.data.provider.LocationProvider
import com.dvt.forecastmvvm.data.provider.LocationProviderImpl
import com.dvt.forecastmvvm.data.provider.UnitProvider
import com.dvt.forecastmvvm.data.provider.UnitProviderImpl
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.dvt.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import com.dvt.forecastmvvm.ui.weather.feature.list.FutureListWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.resocoder.forecastmvvm.data.db.ForecastDatabase
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptor
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

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
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}