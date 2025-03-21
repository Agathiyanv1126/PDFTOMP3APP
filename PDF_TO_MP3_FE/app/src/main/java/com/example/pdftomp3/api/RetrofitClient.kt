package com.example.pdftomp3.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Custom CookieJar to handle session cookies
class SessionCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }
}

// Create an OkHttpClient with cookie handling
private val client = OkHttpClient.Builder()
    .cookieJar(SessionCookieJar()) // Attach the session cookie handler
    .build()

// Modify RetrofitClient to use this client
object RetrofitClient {
    const val BASE_URL = "https://7j1p0s64-8000.inc1.devtunnels.ms/"  // Replace with your actual URL

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // Use session-enabled client
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: ApiService = retrofit.create(ApiService::class.java)
}
