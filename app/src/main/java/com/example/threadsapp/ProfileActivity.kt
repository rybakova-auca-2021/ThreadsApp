package com.example.threadsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.btn_search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }

                R.id.btn_post -> {
                    navController.navigate(R.id.createNewThreadFragment)
                    true
                }

                R.id.btn_like -> {
                    navController.navigate(R.id.activityFragment)
                    true
                }

                R.id.btn_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }

                else -> false
            }
        }
    }
    fun hideBtmNav() {
        val navBar = findViewById<View>(R.id.bottomNavigationView)
        navBar.visibility = View.GONE
    }

    fun showBtmNav() {
        val navBar = findViewById<View>(R.id.bottomNavigationView)
        navBar.visibility = View.VISIBLE
    }
}