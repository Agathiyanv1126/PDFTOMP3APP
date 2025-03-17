package com.example.pdftomp3
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaos.view.PinView
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.ForgotOtpResponse
import com.example.pdftomp3.ui.OtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otp2Activity : AppCompatActivity() {

    var pro: Button? = null
    var bac: ImageView? = null

    private lateinit var pinView: PinView

    private lateinit var sharedPreferences: SharedPreferences

    var storedOtp=0
    var email=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp2)

        pro = findViewById(R.id.Submitbtnotp2)
        bac = findViewById(R.id.backotp2)

        pinView = findViewById(R.id.pinView)


        sharedPreferences = getSharedPreferences("forgotOTP_PREF", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "") ?: ""
        storedOtp = sharedPreferences.getInt("forgotOtp",0)

Log.i("forgotOTP",storedOtp.toString())

        pro?.setOnClickListener {
            val enteredOtp = pinView.text.toString().trim().toIntOrNull()

            if (enteredOtp == storedOtp) {
                verifyOtp(storedOtp)
            } else {
                Toast.makeText(this, "Invalid OTP! Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        bac?.setOnClickListener {
            finish() // Close OTP activity and go back
        }

    }

    private fun verifyOtp(otp: Int) {
        if (email == null) {
            Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.instance.verifyOtp(otp)

        call.enqueue(object : Callback<ForgotOtpResponse> {
            override fun onResponse(call: Call<ForgotOtpResponse>, response: Response<ForgotOtpResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    Log.d("OTP Check", "Received OTP: $storedOtp")

                    Toast.makeText(this@otp2Activity, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@otp2Activity, PasswordActivity::class.java))
                    finish()
                } else {
                    response.body()?.let { it.message?.let { it1 -> Log.i("OTP fail check", it1) } }

                    Toast.makeText(this@otp2Activity, "Invalid OTP!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotOtpResponse>, t: Throwable) {
                Toast.makeText(this@otp2Activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}