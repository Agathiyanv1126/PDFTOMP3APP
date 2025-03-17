package com.example.pdftomp3.ui.favorites

data class FavoritesResponse(
    val status: Boolean,
    val message: String,
    val data: List<FavoriteItem> // ✅ Fix: Use a List
)

data class FavoriteItem(
    val id: String,
    val title: String,
    val file_url: String,
    val date_added: String
)
