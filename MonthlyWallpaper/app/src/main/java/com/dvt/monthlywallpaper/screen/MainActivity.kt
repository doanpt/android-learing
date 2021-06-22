package com.dvt.monthlywallpaper.screen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dvt.monthlywallpaper.R
import com.dvt.monthlywallpaper.base.WallpaperApplication
import com.dvt.monthlywallpaper.data.PreferencesMgn
import com.dvt.monthlywallpaper.di.component.ColorsComponent
import com.dvt.monthlywallpaper.di.component.DaggerColorsComponent
import com.dvt.monthlywallpaper.di.module.ColorsModule
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var application: WallpaperApplication
    @Inject
    lateinit var preferencesMgn: PreferencesMgn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        application = getApplication() as WallpaperApplication
        application.component.inject(this)

        var isVote = preferencesMgn.rated
        Log.d("doan.pt", "Da vote chua $isVote")
    }
}
