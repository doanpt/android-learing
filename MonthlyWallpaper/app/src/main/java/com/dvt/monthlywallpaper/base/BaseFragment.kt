package com.dvt.monthlywallpaper.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

class BaseFragment : Fragment() {
    val activity get() = getActivity() as BaseActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObserver()
    }

    open fun registerObserver() {}
}