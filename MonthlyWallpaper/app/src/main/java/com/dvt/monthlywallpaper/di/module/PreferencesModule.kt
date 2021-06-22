package com.dvt.monthlywallpaper.di.module

import android.content.Context
import com.dvt.monthlywallpaper.data.PreferencesMgn
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule(val context: Context) {

    @Provides
    @Singleton
    fun getPreferences(): PreferencesMgn {
        return PreferencesMgn(context)
    }
}