package com.example.attendenceappteacher

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.attendanceappstudent.data_class.UserProfile
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendenceappteacher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            getUserInfo(token)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserInfo(token: String) {
        ApiClient.getInstance(this).getUserProfile(
            token = token,
            onSuccess = { userProfile ->
                // Handle the success response
                // userProfile is of type UserProfile
                Log.d("UserProfile", "User Profile: ${userProfile.first_name}, ${userProfile.email}")
                setUserData(binding.navView, userProfile)
            },
            onError = { errorMessage ->
                // Handle the error response
                Log.e("UserProfile", "Error: $errorMessage")

                // Show error message to user, for example:
                Toast.makeText(this, "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setUserData(navView: NavigationView, userProfile: UserProfile) {

        // Set the user name and email into the NavigationView header
        val headerView = navView.getHeaderView(0)
        val nameTextView: TextView = headerView.findViewById(R.id.textViewName)
        val emailTextView: TextView = headerView.findViewById(R.id.textViewEmail)
        val phoneTextView: TextView = headerView.findViewById(R.id.textViewPhone)

        nameTextView.text = userProfile.first_name + " " + userProfile.last_name
        emailTextView.text = userProfile.email
        phoneTextView.text = userProfile.phone.toString()
    }
}