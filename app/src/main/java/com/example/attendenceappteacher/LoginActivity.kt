package com.example.attendenceappteacher


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendanceappstudent.data_class.UserLoginRequest
import com.example.attendanceappstudent.data_class.UserLoginResponse
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendenceappteacher.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        if (sharedPreferences.getString("token", null) != null) {
            goToHome()
        }

        binding.signInBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        // Validate input fields
        val email = binding.UniversityId.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Email Required", Toast.LENGTH_LONG).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password Required", Toast.LENGTH_LONG).show()
            return
        }

        // Disable the button to prevent duplicate clicks
        binding.signInBtn.isEnabled = false

        val user = UserLoginRequest(email, password)

        ApiClient.getInstance(this).loginUser(
            user = user,
            onSuccess = { response ->
                saveUserSession(response)

                Toast.makeText(this, "Welcome, ${response.firstName}!", Toast.LENGTH_LONG).show()

                goToHome()

                // Re-enable the button after success
                binding.signInBtn.isEnabled = true
            },
            onError = { error ->
                Toast.makeText(this, "Login failed: $error", Toast.LENGTH_LONG).show()

                // Re-enable the button after failure
                binding.signInBtn.isEnabled = true
            }
        )
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to login
    }

    private fun saveUserSession(response: UserLoginResponse) {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save user details
        editor.putString("token", response.token)
        editor.putString("username", response.username)
        editor.putString("firstName", response.firstName ?: "") // Default to empty string
        editor.putString("lastName", response.lastName ?: "")   // Default to empty string

        // Apply changes
        editor.apply()
    }
}