package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConvertActivity : AppCompatActivity() {
    var con: Button? = null
    var bac: ImageView? = null
    var ico: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_convert)

        con = findViewById(R.id.converttoloading)

        con?.setOnClickListener {
            val intent = Intent(this@ConvertActivity, LoadingActivity::class.java)
            startActivity(intent)
        }

        bac = findViewById(R.id.converttoback)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }

        ico = findViewById(R.id.icoconverttohome)

        ico?.setOnClickListener {
            val intent = Intent(this@ConvertActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}