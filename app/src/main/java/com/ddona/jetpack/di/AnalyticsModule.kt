package com.ddona.jetpack.di

import com.ddona.jetpack.network.AnalyticsService
import com.ddona.jetpack.network.AnalyticsServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {
    @Binds
    public abstract fun bindAnalyticsService(
        analyticsServiceImpl: AnalyticsServiceImpl
    ): AnalyticsService
}