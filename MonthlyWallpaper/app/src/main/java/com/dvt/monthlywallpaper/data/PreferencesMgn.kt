package com.dvt.monthlywallpaper.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Singleton

@Singleton
class PreferencesMgn(context: Context) {

    companion object {
        const val IS_RATED = "is_rated"
    }

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
    }

    var rated
        get() = sharedPreferences.getBoolean(IS_RATED, false)
        set(value) = editor.putBoolean(IS_RATED, value).apply()
}

