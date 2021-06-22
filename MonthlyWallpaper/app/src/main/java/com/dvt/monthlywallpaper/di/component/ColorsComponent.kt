package com.dvt.monthlywallpaper.di.component

import android.content.Context
import com.dvt.monthlywallpaper.di.module.ColorsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ColorsModule::class)])
interface ColorsComponent {
    fun inject(context: Context)
}