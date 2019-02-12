package com.dvt.forecast.data.provider

import com.dvt.forecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}