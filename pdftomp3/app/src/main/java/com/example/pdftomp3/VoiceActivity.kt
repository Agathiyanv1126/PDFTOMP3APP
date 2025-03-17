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
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.VoiceRequest
import com.example.pdftomp3.ui.VoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var maleButton: Button
    private lateinit var femaleButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var pdfId: Int = 0
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)

        maleButton = findViewById(R.id.btnmale)
        femaleButton = findViewById(R.id.btnfemale)
        backButton = findViewById(R.id.voicetoback)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        userId = sharedPreferences.getInt("user_id", -1)
        pdfId = intent.getIntExtra("pdf_id", 0)

        Log.d("VoiceActivity", "onCreate called with userId: $userId, pdfId: $pdfId")

        if (userId == -1 || userId <= 0) {
            showToast("Error: User not logged in. Please log in again.")
            Log.e("VoiceActivity", "Invalid or missing user ID")
            finish()
            return
        }

        if (pdfId <= 0) {
            showToast("Error: Invalid PDF ID.")
            Log.e("VoiceActivity", "Invalid or missing PDF ID")
            finish()
            return
        }

        maleButton.setOnClickListener { sendVoiceSelection("Male") }
        femaleButton.setOnClickListener { sendVoiceSelection("Female") }
        backButton.setOnClickListener { finish() }
    }

    private fun sendVoiceSelection(voiceType: String) {
        if (isFinishing || isDestroyed) {
            Log.e("VoiceActivity", "Activity is finishing/destroyed, skipping API call")
            return
        }

        if (userId <= 0 || pdfId <= 0) {
            showToast("Error: Invalid User ID or PDF ID.")
            Log.e("VoiceActivity", "Invalid userId ($userId) or pdfId ($pdfId)")
            return
        }

        val request = VoiceRequest(userId, pdfId, voiceType)
        Log.d("API_REQUEST", "Sending request: UserID=$userId, PDFID=$pdfId, VoiceType=$voiceType")

        RetrofitClient.instance.sendVoiceSelection(voiceType, userId.toString(), pdfId.toString()).enqueue(object : Callback<VoiceResponse> {
            override fun onResponse(call: Call<VoiceResponse>, response: Response<VoiceResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.Status == true) {
                        val selectedVoice = responseBody.data.SlectedVoice ?: "Unknown"
                        showToast("Voice Selected: $selectedVoice")
                        Log.i("VoiceActivity", "Voice selection successful: $selectedVoice")

                        val intent = Intent(this@VoiceActivity, AudiofilesActivity::class.java)
                        startActivity(intent)
                    } else {
                        val errorMessage = responseBody?.message ?: "Unknown error"
                        showToast("Failed: $errorMessage")
                        Log.e("VoiceActivity", "API error message: $errorMessage")
                    }
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown server error"
                    showToast("Server Error: $errorResponse")
                    Log.e("VoiceActivity", "Full API error response: $errorResponse")
                }
            }

            override fun onFailure(call: Call<VoiceResponse>, t: Throwable) {
                Log.e("VoiceActivity", "Network/API call failed: ${t.message}")
                showToast("Network Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        if (!isFinishing && !isDestroyed) {
            runOnUiThread {
                Toast.makeText(this@VoiceActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
