package com.dvt.forecastmvvm

import android.app.Application
import com.dvt.forecastmvvm.data.network.ApixuWeatherApiService
import com.dvt.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.dvt.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dvt.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.dvt.forecastmvvm.data.repository.ForecastRepository
import com.dvt.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.resocoder.forecastmvvm.data.db.ForecastDatabase
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptor
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = lazy {
        import(androidXModule(this@ForecastApplication))
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance()) }
    }

}