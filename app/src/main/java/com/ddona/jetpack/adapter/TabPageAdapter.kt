package com.ddona.jetpack.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ddona.jetpack.ui.fragment.CountingTwoFragment
import com.ddona.jetpack.ui.fragment.CountingOneFragment

class TabPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CountingOneFragment()
            1 -> CountingTwoFragment()
            else -> throw NotImplementedError()
        }
    }
}