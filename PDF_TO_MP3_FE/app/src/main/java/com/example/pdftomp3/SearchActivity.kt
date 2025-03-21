package com.example.pdftomp3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.SearchRequest
import com.example.pdftomp3.ui.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var searchIcon: ImageView
    private lateinit var searchText: EditText
    private lateinit var listViewResults: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("user_id", 0)

        // Initialize Views
        backButton = findViewById(R.id.searchtoback)
        searchIcon = findViewById(R.id.searchIcon)
        searchText = findViewById(R.id.searchText)
        listViewResults = findViewById(R.id.listViewResults)

        // Handle back button click
        backButton.setOnClickListener { finish() }

        // Set up ListView with an empty adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewResults.adapter = adapter

        // Handle search button click
//        searchIcon.setOnClickListener {
//            val query = searchText.text.toString().trim()
//            if (query.isNotEmpty()) {
//                performSearch(userId, query) // Pass user ID dynamically
//            } else {
//                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

//    private fun performSearch(userId: String, query: String) {
//        val request = SearchRequest(id = userId, query = query)
//
//        RetrofitClient.instance.searchFile(request).enqueue(object : Callback<SearchResponse> {
//            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { searchResponse ->
//                        Log.d("SearchDebug", "API Response: $searchResponse") // Debugging Log
//
//                        val results = mutableListOf<String>()
//
//                        // Extract PDF file title (if available)
//                        searchResponse.data?.pdfFiles?.let {
//                            results.add("PDF: ${it.title}")
//                        }
//
//                        // Extract MP3 file names
//                        searchResponse.data?.mp3Files?.let { mp3Files ->
//                            results.addAll(mp3Files.map { "MP3: $it" })
//                        }
//
//                        // Update ListView with results
//                        adapter.clear()
//                        adapter.addAll(results)
//                        adapter.notifyDataSetChanged()
//
//                        if (results.isEmpty()) {
//                            Toast.makeText(this@SearchActivity, "No results found", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Log.e("SearchDebug", "API Error: ${response.errorBody()?.string()}")
//                    Toast.makeText(this@SearchActivity, "Search failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
//                Log.e("SearchDebug", "Network Error: ${t.message}")
//                Toast.makeText(this@SearchActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}
