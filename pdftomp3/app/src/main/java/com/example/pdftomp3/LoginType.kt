package com.example.pdftomp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginType : AppCompatActivity() {
    var log: Button?=null
    var sig: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_type)


        log=findViewById(R.id.btnLogintype)

        log?.setOnClickListener {
            val intent = Intent(this@LoginType, LoginActivity::class.java)
            startActivity(intent)
        }

            sig=findViewById(R.id.btnSignuplogintype)

            sig?.setOnClickListener {
                val intent = Intent(this@LoginType, SignupActivity::class.java)
                startActivity(intent)

        }
    }
}