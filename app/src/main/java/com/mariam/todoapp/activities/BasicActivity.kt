package com.mariam.todoapp.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mariam.todoapp.R

class BasicActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val currentFragment = intent.getStringExtra("FRAGMENT")
        if(currentFragment == "signup") {
            navController.navigate(R.id.signupFragment)
        }
        else if(currentFragment == "login") {
           navController.navigate(R.id.loginFragment)
        }

        val logoutBtn = this.findViewById<ImageButton>(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            navController.navigate(R.id.loginFragment)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            logoutBtn.visibility = if(destination.id == R.id.todoFragment) View.VISIBLE else View.INVISIBLE
        }
    }
}