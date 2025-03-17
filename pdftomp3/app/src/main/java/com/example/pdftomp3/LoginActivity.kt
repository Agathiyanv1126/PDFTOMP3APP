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
import com.example.pdftomp3.ui.LoginRequest
import com.example.pdftomp3.ui.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var forgotpass: TextView
    private lateinit var Signup: TextView
    private lateinit var btnLogin: Button
    private lateinit var togglePasswordVisibility: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.username)
        etPassword = findViewById(R.id.password)
        forgotpass = findViewById(R.id.forgot_passwordlog)
        btnLogin = findViewById(R.id.btnMainLogin)
        togglePasswordVisibility = findViewById(R.id.ivToggleNewPassword)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


        forgotpass= findViewById(R.id.forgot_passwordlog)

        forgotpass?.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
        }

        Signup= findViewById(R.id.signtext)

        Signup?.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        togglePasswordVisibility(togglePasswordVisibility, etPassword)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
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

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        RetrofitClient.instance.loginUser(username,password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    val userData = response.body()?.data

                    userData?.let {
                        val editor = sharedPreferences.edit()
                        editor.putInt("user_id", it.id)
                        editor.putString("username", it.username)
                        editor.putString("password", it.passwords)
                        editor.putString("email", it.email)


                        Log.i("user_id12",it.id.toString()+it.email+it.username)
                        editor.apply()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }

//              if (response.isSuccessful){
//                   val loginRes = response.body()
//                   loginRes.let {
//                       if (it!!.Status){
//                           val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
//                           val e = sharedPreferences.edit()
//                          e.putString("username", it.data.username)
//                          e.apply()
//
//                          Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()
//                      }
//                   }
//               }
        }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
