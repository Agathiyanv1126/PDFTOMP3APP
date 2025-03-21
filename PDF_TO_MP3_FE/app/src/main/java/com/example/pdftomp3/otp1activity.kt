package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.OtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otp1activity : AppCompatActivity() {
    private lateinit var pro: Button
    private lateinit var bac: ImageView
    private lateinit var pinView: PinView

    var storedOtp=0
    var email=""

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp1activity)

        pro = findViewById(R.id.Submitbtnotp1)
        bac = findViewById(R.id.backotp1)
        pinView = findViewById(R.id.pinView)

        sharedPreferences = getSharedPreferences("OTP_PREF", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "") ?: ""
        storedOtp = sharedPreferences.getInt("otp",0)


        Log.d("OTP Debug", "Received OTP: $storedOtp")

        pro.setOnClickListener {
            val enteredOtp = pinView.text.toString().trim().toIntOrNull()

            if (enteredOtp == storedOtp) {
                verifyOtp(storedOtp)
            } else {
                Toast.makeText(this, "Invalid OTP! Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        bac.setOnClickListener {
            finish() // Close OTP activity and go back
        }
    }

    private fun verifyOtp(otp: Int) {
        if (email == null) {
            Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.instance.signupVerifyOtp(otp)

        call.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    Log.d("OTP Check", "Received OTP: $storedOtp")

                    Toast.makeText(this@otp1activity, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@otp1activity, LoginActivity::class.java))
                    finish()
                } else {
                    response.body()?.let { it.message?.let { it1 -> Log.i("OTP fail check", it1) } }

                    Toast.makeText(this@otp1activity, "Invalid OTP!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                Toast.makeText(this@otp1activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

