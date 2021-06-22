package com.dvt.monthlywallpaper.di.component

import com.dvt.monthlywallpaper.di.module.PreferencesModule
import com.dvt.monthlywallpaper.screen.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(PreferencesModule::class))
interface PreferenceComponent {
    fun inject(activity: MainActivity)
}