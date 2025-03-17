package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChangedActivity : AppCompatActivity() {
    var pro: Button? = null
    var bac: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_changed)

        pro = findViewById(R.id.btngoLogin)

        pro?.setOnClickListener {
            val intent = Intent(this@ChangedActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        bac = findViewById(R.id.backchanged)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }
    }
}