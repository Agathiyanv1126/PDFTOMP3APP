package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.SignupRequest
import com.example.pdftomp3.ui.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnNext: Button
    private lateinit var togglePasswordVisibility: ImageView
    private lateinit var tvLogin: TextView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etFirstName = findViewById(R.id.firstName)
        etLastName = findViewById(R.id.lastName)
        etUsername = findViewById(R.id.username)
        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.password)
        btnNext = findViewById(R.id.Nextlog)
        tvLogin = findViewById(R.id.logitext)
        togglePasswordVisibility = findViewById(R.id.toggleshow)

        sharedPreferences = getSharedPreferences("OTP_PREF", Context.MODE_PRIVATE)


        btnNext.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            Log.i("details12", firstName + lastName + username + email + password)

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signupUser(firstName, lastName, username, email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        togglePasswordVisibility(togglePasswordVisibility, etPassword)

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun togglePasswordVisibility(toggleView: ImageView, passwordField: EditText) {
        var isPasswordVisible = false

        toggleView.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordField.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleView.setImageResource(R.drawable.show) // Change to hide icon
            } else {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggleView.setImageResource(R.drawable.show) // Change to show icon
            }
            passwordField.setSelection(passwordField.text.length) // Maintain cursor position
        }
    }

    private fun signupUser(firstName: String, lastName: String, username: String, email: String, password: String) {
        val call = RetrofitClient.instance.signupUser(firstName, lastName, username, email, password)

        Log.i("details", firstName + lastName + username + email + password)

        call.enqueue(object : Callback<SignupResponse> {


            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    val userData = response.body()?.data
                    userData?.let {



                        val otp = userData.otp // Assuming the OTP is returned in the response
                        sharedPreferences.edit().putInt("otp", otp).apply()
                        sharedPreferences.edit().putString("email", email).apply()
                        Log.i("OTP_Saved", otp.toString())

                        val i = Intent(this@SignupActivity, otp1activity::class.java)
                        i.putExtra("mail", email)
                        i.putExtra("otp", otp) // Pass the OTP to the next activity
                        startActivity(i)
                        finish()
                    }

                } else {
                    Toast.makeText(this@SignupActivity, "OTP sending failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Network timeout! Retrying...", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
