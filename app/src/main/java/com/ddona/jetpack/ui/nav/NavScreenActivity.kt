package com.ddona.jetpack.ui.nav

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphNavigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.ddona.jetpack.R
import com.ddona.jetpack.databinding.ActivityNavBinding

class NavScreenActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityNavBinding
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        //create appbar to remove back button on search fragment
        appBarConfig = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.searchFragment),
            //add this to show home icon on action bar
            binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfig)

        binding.bottomNav.setupWithNavController(navController)

        binding.navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.termAndConditions) {
            navController.navigate(R.id.action_global_termsFragment)
            return true
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        //add this to handle click on home icon on action bar
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}