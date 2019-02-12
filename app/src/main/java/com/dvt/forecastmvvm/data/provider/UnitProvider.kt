package com.dvt.forecastmvvm.data.provider

import com.dvt.forecastmvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}