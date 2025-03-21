package com.example.pdftomp3

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrivacyActivity : AppCompatActivity() {
    var bac: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy)
        bac = findViewById(R.id.privacytoback)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }
    }
}