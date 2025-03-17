package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.ForgotOtpRequest
import com.example.pdftomp3.ui.ForgotOtpResponse
import com.example.pdftomp3.ui.OtpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {
    private lateinit var pro: Button
    private lateinit var bac: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var otpsharedPreferences: SharedPreferences

    var username=""
    var email=""
    private var txtEmail:TextView?=null
    private var txtUsername:TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_actvity)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        email = sharedPreferences.getString("email", "").toString()

        Log.i("email & pass", username+email)


        pro = findViewById(R.id.sendOtpButtonf)
        bac = findViewById(R.id.backforg)
        txtEmail = findViewById(R.id.txtEmail)
        txtUsername = findViewById(R.id.txtUsername)

        txtEmail?.text = email
        txtUsername?.text = username

        pro.setOnClickListener {
            sendOtp(email)
        }

        bac.setOnClickListener {
            finish()
        }
    }

    private fun sendOtp(email: String) {
        val call = RetrofitClient.instance.sendOtp(email,username)

        call.enqueue(object : Callback<ForgotOtpResponse> {
            override fun onResponse(call: Call<ForgotOtpResponse>, response: Response<ForgotOtpResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    val userData = response.body()?.data
                    userData?.let {


                        val otp = userData.OTP

                        sharedPreferences = getSharedPreferences("forgotOTP_PREF", Context.MODE_PRIVATE)

                        sharedPreferences.edit().putInt("forgotOtp", otp).apply()
                        sharedPreferences.edit().putString("email", email).apply()
                        Log.i("ForgotOTP_Saved", otp.toString())

                        val i = Intent(this@ForgotActivity, otp2Activity::class.java)
                        i.putExtra("mail", email)
                        i.putExtra("otp", otp) // Pass the OTP to the next activity
                        startActivity(i)
                        finish()
                    }

                } else {
                    Toast.makeText(this@ForgotActivity, "OTP sending failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotOtpResponse>, t: Throwable) {
                Toast.makeText(this@ForgotActivity, "API call failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
  }
}
