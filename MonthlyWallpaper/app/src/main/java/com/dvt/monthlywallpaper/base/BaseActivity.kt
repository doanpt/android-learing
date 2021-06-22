package com.dvt.monthlywallpaper.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BaseActivity :  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerObserver()
    }
    open fun registerObserver(){}
}