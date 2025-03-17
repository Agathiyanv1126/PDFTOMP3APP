package com.example.pdftomp3

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val Status: Boolean,
    val message: String,
    val data: List<FileCategory>
)

data class FileCategory(
    @SerializedName("PDF Files") val pdfFiles: List<FileData>?,
    @SerializedName("MP3 Files") val mp3Files: List<FileData>?
)

data class FileData(
    val id: Int,
    val user_id: Int,
    val title: String,
    @SerializedName("pdf_file") val pdfFile: String?,
    @SerializedName("mp3_file") val mp3File: String?,
    val date: String
)
