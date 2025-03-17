package com.example.pdftomp3.api

import com.example.pdftomp3.ApiResponse
import com.example.pdftomp3.models.AudioFilesRequest
import com.example.pdftomp3.models.AudioFilesResponse
import com.example.pdftomp3.ui.AddFavoriteResponse
import com.example.pdftomp3.ui.ForgotOtpResponse
import com.example.pdftomp3.ui.LoginRequest
import com.example.pdftomp3.ui.LoginResponse
import com.example.pdftomp3.ui.OtpRequest
import com.example.pdftomp3.ui.OtpResponse
import com.example.pdftomp3.ui.OtpResponses
import com.example.pdftomp3.ui.ResetPasswordRequest
import com.example.pdftomp3.ui.ResetPasswordResponse
import com.example.pdftomp3.ui.SearchRequest
import com.example.pdftomp3.ui.SearchResponse
import com.example.pdftomp3.ui.SignupRequest
import com.example.pdftomp3.ui.SignupResponse
import com.example.pdftomp3.ui.UpdateUserRequest
import com.example.pdftomp3.ui.UpdateUserResponse
import com.example.pdftomp3.ui.UploadResponse
import com.example.pdftomp3.ui.UserResponse
import com.example.pdftomp3.ui.VoiceRequest
import com.example.pdftomp3.ui.VoiceResponse
import com.example.pdftomp3.ui.favorites.FavoritesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

    @FormUrlEncoded
    @POST("Login")
    fun loginUser(@Field("username") username: String,
                  @Field("password") password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("Signup")
    fun signupUser(@Field("first_name") first_name: String,
                   @Field("last_name") last_name: String,
                   @Field("username") username: String,
                   @Field("email") email: String,
                   @Field("password") password: String): Call<SignupResponse>

    @FormUrlEncoded
    @POST("sendotp")  // Adjust the endpoint as per your API
    fun sendOtp(
        @Field("email") email: String,
        @Field("username") username: String  // Include username in request
    ): Call<ForgotOtpResponse>

    @FormUrlEncoded
    @POST("verifyotp")
    fun verifyOtp(@Field("otp") otp: Int): Call<ForgotOtpResponse>

    @FormUrlEncoded
    @POST("Voice")  // Replace with actual API endpoint
    fun sendVoiceSelection (@Field("voiceType") voiceType: String,
                            @Field("id") id: String,
                            @Field("pdf_id") pdf_id: String): Call<VoiceResponse>


    @FormUrlEncoded
    @POST("favorite")
    fun getFavorites(@Field("id") userId: Int): Call<FavoritesResponse>

    @FormUrlEncoded
    @POST("files")
    fun getAllFiles(@Field("id") userId: Int): Call<ApiResponse>

    @FormUrlEncoded
    @POST("audiofiles")
    fun getAudioFiles(@Field("id") id: Int): Call<AudioFilesResponse>

    @FormUrlEncoded
    @POST("verify")  // Ensure this matches your backend API endpoint
    fun signupVerifyOtp(@Field("otp") otp: Int): Call<OtpResponse>

    @FormUrlEncoded
    @POST("profile")  // API endpoint
    fun getUserDetails(
        @Field("id") userId: Int
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("edit")
    fun updateUserDetails(@Field("id") userId: String,
                          @Field("name") name:String,
                          @Field("username") username:String,
                          @Field("mobile_phone") mobile_phone:String,
                          @Field("email") email:String
    ): Call<UpdateUserResponse>

    @Multipart  // ✅ Add this to support file upload
    @POST("upload")
    fun uploadPdf(
        @Part pdf_file: MultipartBody.Part,
        @Part("id") id: RequestBody  // ✅ Ensure it's passed as RequestBody
    ): Call<UploadResponse>

    @POST("search")
    fun searchFile(@Field("id") userId: String,
                   @Field("name") name:String
    ): Call<SearchResponse>

    @FormUrlEncoded
    @POST("addfavorite")
    fun addFavorite(@Field("id") userId: Int,
                    @Field("mp3_id") mp3Id:Int): Call<AddFavoriteResponse>

    @FormUrlEncoded
    @POST("resetpassword")
    fun resetPassword(@Field("new_password") newPassword: String,
                      @Field("confirm_password") confirmPassword:String): Call<ResetPasswordResponse>

}





