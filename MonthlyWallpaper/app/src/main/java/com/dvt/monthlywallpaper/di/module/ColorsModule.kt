package com.dvt.monthlywallpaper.di.module

import android.content.Context
import com.dvt.monthlywallpaper.R
import dagger.Module
import dagger.Provides

@Module
class ColorsModule(val context: Context) {
    @Provides
    fun getColors() = context.resources.getStringArray(R.array.colors_label)
}