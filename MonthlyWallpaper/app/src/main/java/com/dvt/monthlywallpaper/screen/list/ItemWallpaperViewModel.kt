package com.dvt.monthlywallpaper.screen.list

import com.dvt.monthlywallpaper.base.WallpaperViewModel
import com.dvt.monthlywallpaper.data.model.Wallpaper

class ItemWallpaperViewModel(wallpaper: Wallpaper, val onItemClick: (item: Wallpaper) -> Unit) : WallpaperViewModel(wallpaper) {
}