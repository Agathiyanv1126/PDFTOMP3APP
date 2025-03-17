package com.example.pdftomp3.ui

import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName


data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val Status: Boolean,
    val message: String,
    val data: UserData
)

data class UserData(
    val id: Int,
    val username: String,
    val passwords: String,
    val email:String
)


data class SignupRequest(
    val first_name: String,
    val last_name: String,
    val username: String,
    val email: String,
    val password: String
)

// Response Data Model
data class SignupResponse(
    val Status: Boolean,
    val message: String,
    val data: Otpdata
)

// OTP Data Model
data class Otpdata(
    val otp: Int
)
data class OtpRequest(
    val email: String
)


data class OtpResponse(
    val Status: Boolean,
    val message: String?,
    val data: OtpData1?
)

data class ResetPasswordRequest(
    val username: String = "Agasth",
    val new_password: String,
    val confirm_password: String
)

data class ResetPasswordResponse(
    val Status: Boolean,
    val message: String
)



data class OtpData1(
    val otp: String
)

data class OtpRequests(
    val email: String,
    val otp: String
)

data class OtpResponses(
    val Status: Boolean,
    val message: String
)

data class VoiceRequest(
    val id: Int,
    val pdf_id:Int,
    val voiceType: String
)


// Data class for API response
data class VoiceResponse(
    val Status: Boolean,
    val message: String,
    val data: VoiceData
)

// Data class for the inner "data" field
data class VoiceData(
    val SlectedVoice: String // Example: "Male"
)


data class UpdateUserRequest(
    val id: Int,
    val name: String,
    val username: String,
    val mobile_phone: String,
    val email: String,
)

data class UpdateUserResponse(
    val Status: Boolean,
    val message: String,
    val data: UpdateUserData?
)

data class UpdateUserData(
    val id: Int,
    val name: String,
    val username: String,
    val mobile_phone: String,
    val email: String,
)


data class UserResponse(
    val Status: Boolean,
    val message: String,
    val data: UserDetails
)

data class UserDetails(
    val id: Int,
    val name: String,
    val username: String,
    val mobile_phone: String,
    val email: String
)

data class UploadResponse(
    val Status: Boolean,
    val message: String,
    val data: UploadData?
)

data class UploadData(
    val id: Int,
    val title: String,
    val date: String
)



data class SearchRequest(
    val id: String,
    val query: String
)

data class SearchResponse(
    val status: Boolean,
    val message: String,
    val data: SearchData? // ✅ Use the correct data model
)

data class SearchData(
    val pdfFiles: PdfFile?, // ✅ Corrected PdfFile structure
    val mp3Files: List<String>? // ✅ MP3 files are returned as a list of strings
)

data class PdfFile(
    val id: Int,
    val pdf_file: String,
    val title: String,
    val date: String
)



// ✅ Correct Request Model
data class ForgotOtpRequest(
    val email: String,
    val username: String
)

// ✅ Correct Response Model
data class ForgotOtpResponse(
    val Status: Boolean,
    val message: String?,
    val data: otpDatas
)

data class otpDatas(
    val OTP: Int // ✅ Ensure this matches API response type
)

data class AddFavoriteResponse(
    val Status: Boolean,
    val message: String,
    val data: FavoriteData?
)

data class FavoriteData(
    @SerializedName("MP3 FILES") // 🔹 Fix: Map JSON key with a space to a valid variable name
    val mp3Files: Mp3FileDetails
)

data class Mp3FileDetails(
    val id: Int,
    val mp3_file: String,
    val title: String,
    val date: String
)
data class FileData(
    val id: Int,
    val title: String,
    val date: String,
    val fileUri: String // <-- Add this property
)
