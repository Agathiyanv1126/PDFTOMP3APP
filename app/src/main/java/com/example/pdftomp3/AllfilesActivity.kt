package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdftomp3.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllfilesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bac: ImageView
    private lateinit var add: ImageView
    private lateinit var sort: ImageView

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allfiles)

        // Initialize UI elements
        bac = findViewById(R.id.allfilestoback)
        add = findViewById(R.id.addfiles)
        sort = findViewById(R.id.allfilestosort)
        recyclerView = findViewById(R.id.recyclerView)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getInt("user_id", 0)

        Log.i("USER_ID", "User ID: $userId")


        recyclerView.layoutManager = LinearLayoutManager(this)

        // Button Click Listeners
        bac.setOnClickListener { finish() }
        add.setOnClickListener { startActivity(Intent(this, UploadfilesActivity::class.java)) }
        sort.setOnClickListener { startActivity(Intent(this, SortActivity::class.java)) }

        // Fetch files for user ID 4
        fetchFiles(userId)
    }

    private fun fetchFiles(userId: Int) {
        RetrofitClient.instance.getAllFiles(userId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    val allFiles = mutableListOf<FileData>()

                    response.body()?.data?.forEach { category ->
                        category.pdfFiles?.let { allFiles.addAll(it) }
                        category.mp3Files?.let { allFiles.addAll(it) }
                    }

                    recyclerView.adapter = AllfilesAdapter(allFiles)
                } else {
                    Toast.makeText(this@AllfilesActivity, "Failed to load files", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@AllfilesActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
