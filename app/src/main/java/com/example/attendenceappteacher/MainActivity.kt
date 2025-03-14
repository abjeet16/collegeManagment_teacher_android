package com.example.attendenceappteacher

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_internals
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserInfo(token: String) {
        ApiClient.getInstance(this).getUserProfile(
            token = token,
            onSuccess = { userProfile ->
                Log.d("UserProfile", "User Profile: ${userProfile.first_name}, ${userProfile.email}")
                setUserData(binding.navView, userProfile)
            },
            onError = { errorMessage ->
                Log.e("UserProfile", "Error: $errorMessage")
                Toast.makeText(this, "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setUserData(navView: NavigationView, userProfile: UserProfile) {
        val headerView = navView.getHeaderView(0)
        val nameTextView: TextView = headerView.findViewById(R.id.textViewName)
        val emailTextView: TextView = headerView.findViewById(R.id.textViewEmail)
        val phoneTextView: TextView = headerView.findViewById(R.id.textViewPhone)

        nameTextView.text = userProfile.first_name + " " + userProfile.last_name
        emailTextView.text = userProfile.email
        phoneTextView.text = userProfile.phone.toString()
    }

    private fun performLogout() {
        // Show confirmation dialog
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to log out?")

        // Set up the "Yes" button
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Remove the token from SharedPreferences
            sharedPreferences.edit().remove("token").apply()

            // Show a toast message to indicate successful logout
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity and finish MainActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss() // Close the dialog
        }

        // Set up the "No" button
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Close the dialog
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}
