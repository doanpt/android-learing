package com.dvt.monthlywallpaper.base

import android.app.Application
import com.dvt.monthlywallpaper.data.PreferencesMgn
import com.dvt.monthlywallpaper.di.component.DaggerPreferenceComponent
import com.dvt.monthlywallpaper.di.component.PreferenceComponent
import com.dvt.monthlywallpaper.di.module.PreferencesModule
class WallpaperApplication : Application() {
    lateinit var component: PreferenceComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerPreferenceComponent.builder().preferencesModule(PreferencesModule(applicationContext)).build()
    }
}