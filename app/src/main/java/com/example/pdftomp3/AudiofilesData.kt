package com.example.pdftomp3.models

import com.google.gson.annotations.SerializedName

data class AudioFilesRequest(val id: String)

data class AudioFilesResponse(
    val Status: Boolean,
    val message: String,
    val data: List<AudioDataWrapper>
)

data class AudioDataWrapper(
    @SerializedName("MP3 Files") val mp3Files: List<AudioFile> // Use camelCase for Kotlin variable
)

data class AudioFile(
    val id: Int,
    val user_id: Int?,  // Nullable Int to handle possible null values
    val title: String,
    @SerializedName("mp3_file") val mp3File: String, // Use camelCase for Kotlin variable
    val date: String
)
