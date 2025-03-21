package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.ResetPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var backButton: ImageView
    private lateinit var ivToggleNewPassword: ImageView
    private lateinit var ivToggleConfirmPassword: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password)

        // Initialize UI components
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnResetPassword = findViewById(R.id.resetpasswordbtn)
        backButton = findViewById(R.id.backbtnpass)
        ivToggleNewPassword = findViewById(R.id.ivToggleNewPassword)
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword)

        // Handle password visibility toggle
        togglePasswordVisibility(ivToggleNewPassword, etNewPassword)
        togglePasswordVisibility(ivToggleConfirmPassword, etConfirmPassword)

        // Handle reset password button click
        btnResetPassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validatePasswords(newPassword, confirmPassword)) {
                resetPassword(newPassword, confirmPassword)
            }
        }

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Closes the current activity and goes back
        }
    }

    // Function to toggle password visibility
    private fun togglePasswordVisibility(toggleView: ImageView, passwordField: EditText) {
        var isPasswordVisible = false

        toggleView.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordField.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleView.setImageResource(R.drawable.show) // Use an eye-open icon
            } else {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggleView.setImageResource(R.drawable.show) // Use an eye-close icon
            }
            passwordField.setSelection(passwordField.text.length) // Maintain cursor position
        }
    }

    // Function to validate passwords before making API call
    private fun validatePasswords(newPassword: String, confirmPassword: String): Boolean {
        return when {
            newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Please enter both passwords", Toast.LENGTH_SHORT).show()
                false
            }
            newPassword.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            newPassword != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    // Function to call API and reset password
    private fun resetPassword(newPassword: String, confirmPassword: String) {
        RetrofitClient.instance.resetPassword(newPassword, confirmPassword)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        Toast.makeText(applicationContext, result?.message, Toast.LENGTH_LONG).show()

                        // Navigate to ChangedActivity if password reset is successful
                        if (result?.Status == true) {
                            startActivity(Intent(this@PasswordActivity, ChangedActivity::class.java))
                            finish() // Close current activity
                        }
                    } else {
                        Toast.makeText(applicationContext, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
