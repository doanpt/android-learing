package com.dvt.monthlywallpaper.screen.list

import android.arch.lifecycle.ViewModel
import com.dvt.monthlywallpaper.R
import com.dvt.monthlywallpaper.data.model.Wallpaper
import com.dvt.monthlywallpaper.util.Color
import javax.inject.Inject

class WallpapersViewModel : ViewModel() {
    private val groupOfImageIdAndName = arrayOf(
            Pair(R.drawable.january, "January"),
            Pair(R.drawable.february, "February"),
            Pair(R.drawable.march, "March"),
            Pair(R.drawable.april, "April"),
            Pair(R.drawable.may, "May"),
            Pair(R.drawable.june, "June"),
            Pair(R.drawable.july, "July"),
            Pair(R.drawable.august, "August"),
            Pair(R.drawable.september, "September"),
            Pair(R.drawable.october, "October"),
            Pair(R.drawable.november, "November"),
            Pair(R.drawable.december, "December")
    )
    @Inject
    lateinit var color: Color

    fun getCount() = wallpapers.size
    fun hasBeenInitialized() = ::color.isInitialized
    //    private val wallpapers by lazy { Array(groupOfImageIdAndName.size, { i -> { Wallpaper(groupOfImageIdAndName[i].first, groupOfImageIdAndName[i].second, "Material design", Color.BLACK) } }) }
    private val wallpapers by lazy { Array(groupOfImageIdAndName.size) { i -> { Wallpaper(groupOfImageIdAndName[i].first, groupOfImageIdAndName[i].second, "Material design", color.getColorAt(i)) } } }

    fun onItemWallpaperClick(wallpaper: Wallpaper) {

    }
}