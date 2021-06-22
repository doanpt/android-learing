package com.dvt.monthlywallpaper.base

import com.dvt.monthlywallpaper.data.model.Wallpaper

open class WallpaperViewModel(val wallpaper: Wallpaper) {
    fun getImageId() = wallpaper.imageId
    fun getImageName() = wallpaper.name
    fun getImageAuthor() = wallpaper.author
}