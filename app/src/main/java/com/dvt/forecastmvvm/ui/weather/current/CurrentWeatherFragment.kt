package com.dvt.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.dvt.forecastmvvm.R
import com.dvt.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

//    neu su dung  GlobalScope.launch {  } se loi
//    java.lang.IllegalStateException: Cannot invoke observe on a background thread
//    at androidx.lifecycle.LiveData.assertMainThread(LiveData.java:443)
//    at androidx.lifecycle.LiveData.observe(LiveData.java:171)
//    at com.dvt.forecastmvvm.ui.weather.current.CurrentWeatherFragment$bindUI$1.invokeSuspend(CurrentWeatherFragment.kt:44)
    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            tv_current.text = it.toString()
        })
    }

}