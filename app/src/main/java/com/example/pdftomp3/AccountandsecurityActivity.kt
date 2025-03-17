package com.example.pdftomp3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.UpdateUserResponse
import com.example.pdftomp3.ui.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountandsecurityActivity : AppCompatActivity() {
    private lateinit var submitButton: Button
    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etMobile: EditText
    private lateinit var etEmail: EditText
    private lateinit var backButton: ImageView
    private lateinit var sharedPreferences: SharedPreferences

     // User ID from SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountandsecurity)

        // Initialize UI elements
        submitButton = findViewById(R.id.Submittochange)
        etName = findViewById(R.id.nameentry)
        etUsername = findViewById(R.id.userentry)
        etMobile = findViewById(R.id.noentry)
        etEmail = findViewById(R.id.mailentry)
        backButton = findViewById(R.id.accounttoback)

        // Get stored user ID
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getInt("user_id", 0)

        Log.i("USER_ID", "User ID: $userId")

        // Load user details
        getUserDetails(userId)

        // Submit button click event
        submitButton.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val mobilePhone = etMobile.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isNotEmpty() && username.isNotEmpty() && mobilePhone.isNotEmpty() && email.isNotEmpty()) {
                updateUserDetails(userId, name, username, mobilePhone, email)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Back button click event
        backButton.setOnClickListener {
            finish()
        }
    }

    // Fetch user details
    private fun getUserDetails(id: Int) {
        RetrofitClient.instance.getUserDetails(id).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.data
                    etName.setText(user.name)
                    etUsername.setText(user.username)
                    etMobile.setText(user.mobile_phone)
                    etEmail.setText(user.email)
                    Log.d("API_SUCCESS", "User loaded: ${user.name}, ${user.email}")
                } else {
                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to fetch user details: ${t.message}")
            }
        })
    }

    // Update user details
    private fun updateUserDetails(id: Int, name: String, username: String, mobile: String, email: String) {
        Log.i("USER_UPDATE", "Updating user: ID=$id, Name=$name, Username=$username, Mobile=$mobile, Email=$email")

        RetrofitClient.instance.updateUserDetails(id.toString(), name, username, mobile, email)
            .enqueue(object : Callback<UpdateUserResponse> {
                override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                    if (response.isSuccessful && response.body()?.Status == true) {
                        Toast.makeText(this@AccountandsecurityActivity, "Updated successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API_UPDATE_ERROR", "Update failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@AccountandsecurityActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                    Log.e("API_UPDATE_FAILURE", "Update request failed: ${t.message}")
                    Toast.makeText(this@AccountandsecurityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
