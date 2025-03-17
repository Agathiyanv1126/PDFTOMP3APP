package com.example.pdftomp3

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HelpandsupportActivity : AppCompatActivity() {

    var  bac:ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_helpandsupport)

        bac = findViewById(R.id.helptoback)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }
    }
}