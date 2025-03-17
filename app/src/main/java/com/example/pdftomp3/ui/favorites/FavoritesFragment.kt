package com.example.pdftomp3.ui.favorites

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdftomp3.R
import com.example.pdftomp3.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter
    private var favoriteList = mutableListOf<FavoriteItem>()
    private lateinit var sharedPreferences: SharedPreferences
    var name=0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        name= sharedPreferences.getInt("user_id",0)

        Log.i("userid11", name.toString())


        adapter = FavoritesAdapter(favoriteList)
        recyclerView.adapter = adapter

        fetchFavorites(name)

        return view
    }

    private fun fetchFavorites(n:Int) {
        RetrofitClient.instance.getFavorites(n).enqueue(object : Callback<FavoritesResponse> {
            override fun onResponse(call: Call<FavoritesResponse>, response: Response<FavoritesResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true && !responseBody.data.isNullOrEmpty()) {
                        favoriteList.clear()
                        favoriteList.addAll(responseBody.data) // ✅ Fix: Directly use the list
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "No favorites found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(requireContext(), "Failed to load favorites: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FavoritesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
