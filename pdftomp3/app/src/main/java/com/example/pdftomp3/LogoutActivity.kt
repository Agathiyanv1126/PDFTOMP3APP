package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LogoutActivity : AppCompatActivity() {
    var log: Button?=null
    var bac:ImageView?=null
    var no:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout)

        no = findViewById(R.id.logouttoback)

        no?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }

        bac = findViewById(R.id.logouttoback)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }
        log=findViewById(R.id.btnyes)

        log?.setOnClickListener {
            val intent = Intent(this@LogoutActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}