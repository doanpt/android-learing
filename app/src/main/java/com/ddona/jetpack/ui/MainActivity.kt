package com.ddona.jetpack.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
//import androidx.databinding.DataBindingUtil
import com.ddona.jetpack.R
import com.ddona.jetpack.adapter.TabPageAdapter
import com.ddona.jetpack.databinding.ActivityMainBinding
import com.ddona.jetpack.lifecycle.MyLifeCycleObserver
import com.ddona.jetpack.util.MyLogger
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val tabTitles = listOf("CountOne", "CountTwo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Note, with this way, we need to convert xml layout to support data binding
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.navMain.setNavigationItemSelectedListener(this)


        binding.vpMain.adapter = TabPageAdapter(this)
        TabLayoutMediator(binding.tapMain, binding.vpMain) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        val lifeCycleObserver = MyLifeCycleObserver(lifecycle, MyLogger())
        lifecycle.addObserver(lifeCycleObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.openDrawer(GravityCompat.START)
            } else {
                binding.drawer.closeDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemList) {
            binding.vpMain.currentItem = 0
        } else if (item.itemId == R.id.itemFavorite) {
            binding.vpMain.currentItem = 2
        } else if (item.itemId == R.id.itemAbout) {
            Toast.makeText(this, "Feature are developing!", Toast.LENGTH_SHORT).show()
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

}