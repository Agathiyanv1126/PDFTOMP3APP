package com.example.pdftomp3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.models.AudioFilesRequest
import com.example.pdftomp3.models.AudioFilesResponse
import com.example.pdftomp3.models.AudioFile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudiofilesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var audioAdapter: Audiofilesadapter
    var bac: ImageView? = null
    private val audioList = mutableListOf<AudioFile>()
    private lateinit var searchView: SearchView

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audiofiles)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getInt("user_id", 0)

        Log.i("USER_ID", "User ID: $userId")

        searchView = findViewById(R.id.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    audioAdapter.filter(newText)
                }
                return true
            }
        })

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        audioAdapter = Audiofilesadapter(audioList, this) // ✅ Ensure correct data type
        recyclerView.adapter = audioAdapter

        fetchAudioFiles(userId)

        bac = findViewById(R.id.audiofilestoback)

        bac?.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous one
        }
    }

    private fun fetchAudioFiles(userId: Int) {

        RetrofitClient.instance.getAudioFiles(userId).enqueue(object : Callback<AudioFilesResponse> {
            override fun onResponse(call: Call<AudioFilesResponse>, response: Response<AudioFilesResponse>) {
                if (response.isSuccessful && response.body()?.Status == true) {
                    response.body()?.data?.forEach { wrapper ->
                        wrapper.mp3Files.forEach { file ->
                            audioList.add(file)
                        }
                    }
                    audioAdapter.updateList(audioList) // ✅ Update the adapter with new data
                } else {
                    Toast.makeText(this@AudiofilesActivity, "Failed to retrieve files", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AudioFilesResponse>, t: Throwable) {
                Toast.makeText(this@AudiofilesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Error fetching files", t)
            }
        })
    }
}
