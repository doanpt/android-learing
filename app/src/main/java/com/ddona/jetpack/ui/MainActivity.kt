package com.ddona.jetpack.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.ddona.jetpack.R
import com.ddona.jetpack.adapter.TabPageAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val tabTitles = listOf("Reddit", "Counting")
    private lateinit var drawer: DrawerLayout
    private lateinit var navMain: NavigationView
    private lateinit var vpMain: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabMain = findViewById<TabLayout>(R.id.tapMain)
        drawer = findViewById(R.id.drawer)
        vpMain = findViewById<ViewPager2>(R.id.vpMain)
        navMain = findViewById(R.id.navMain)
        navMain.setNavigationItemSelectedListener(this)


        vpMain.adapter = TabPageAdapter(this)
        TabLayoutMediator(tabMain, vpMain) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START)
            } else {
                drawer.closeDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemList) {
            vpMain.currentItem = 0
        } else if (item.itemId == R.id.itemFavorite) {
            vpMain.currentItem = 2
        } else if (item.itemId == R.id.itemAbout) {
            Toast.makeText(this, "Feature are developing!", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}