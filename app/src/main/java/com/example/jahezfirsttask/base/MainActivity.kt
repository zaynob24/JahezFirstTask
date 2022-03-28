package com.example.jahezfirsttask.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
         navController = navHostFragment.navController

    }

    // fix bug of press back in home page (RestaurantListFragment) ,,app close
    // so instead of app go back to privies page ,, app close
    override fun onBackPressed() {
        when(navController.currentDestination?.id) {
            R.id.restaurantListFragment -> finish()
            else -> super.onBackPressed()
        }
    }
}
