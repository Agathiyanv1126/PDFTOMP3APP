package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_activity)

        // Delay for 3 seconds, then move to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@LoadingActivity, musicplayerActivity::class.java)
            startActivity(intent)
            finish() // Close the loading screen
        }, 3000) // 3 seconds delay
    }
}
