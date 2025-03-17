package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class UploadfilesActivity : AppCompatActivity() {

    private lateinit var btnConvert: Button
    private lateinit var btnBrowse: Button
    private lateinit var btnView: Button
    private lateinit var btnBack: ImageView
    private lateinit var txtCancel: TextView
    private lateinit var txtHome: TextView
    private lateinit var txtSelectedFile: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = 0  // Store user ID from SharedPreferences

    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadfiles)

        // Load user ID from SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("user_id", 0) // Default to 0 if not found

        Log.i("UserID", "Retrieved User ID: $userId")

        btnBrowse = findViewById(R.id.btnBrowse)
        btnConvert = findViewById(R.id.uploadtoconvert)
        btnView = findViewById(R.id.btnView)
        btnBack = findViewById(R.id.uploadtoback)
        txtCancel = findViewById(R.id.uptoback)
        txtHome = findViewById(R.id.icouploadtohome)
        txtSelectedFile = findViewById(R.id.selectedFileText)

        btnView.isEnabled = false // Disable View button initially

        // File Picker
        btnBrowse.setOnClickListener {
            pickPdfFile()
        }

        // Convert Button
        btnConvert.setOnClickListener {
            selectedFileUri?.let {
                uploadFile(it, userId)
            } ?: showAlert("No file selected", "Please select a PDF file first.")
        }

        // View Button
        btnView.setOnClickListener {
            selectedFileUri?.let {
                viewSelectedFile(it)
            } ?: showAlert("No file selected", "Please select a file first.")
        }

        // Close Activity (Back & Cancel)
        btnBack.setOnClickListener { finish() }
        txtCancel.setOnClickListener { finish() }

        // Navigate to Home
        txtHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // File Picker
    private val pickPdfLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            showConfirmationDialog(it)
        }
    }

    private fun pickPdfFile() {
        pickPdfLauncher.launch(arrayOf("application/pdf"))
    }

    private fun showConfirmationDialog(uri: Uri) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Selection")
            .setMessage("Do you want to select this file?")
            .setPositiveButton("Yes") { _, _ ->
                selectedFileUri = uri
                txtSelectedFile.text = "Selected File: ${getFileName(uri)}"
                btnView.isEnabled = true
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: uri.lastPathSegment ?: "Unknown File"
    }

    private fun viewSelectedFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Open with"))
    }

    private fun uploadFile(fileUri: Uri, userId: Int) {
        val contentResolver = applicationContext.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)

        inputStream?.use { stream ->
            val fileBytes = stream.readBytes()
            val requestBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())

            val filePart = MultipartBody.Part.createFormData(
                "pdf_file", getFileName(fileUri), requestBody
            )

            // ✅ Convert userId to RequestBody
            val idPart = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            Log.i("UploadFile", "Uploading file: ${getFileName(fileUri)} with ID: $userId")

            RetrofitClient.instance.uploadPdf(filePart, idPart)
                .enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                        if (response.isSuccessful) {
                            val uploadData = response.body()?.data
                            Toast.makeText(this@UploadfilesActivity,response.body()?.message, Toast.LENGTH_SHORT).show()
                            val i = Intent(this@UploadfilesActivity, VoiceActivity::class.java)
                            i.putExtra("pdf_id",uploadData?.id)
                            startActivity(i)

                        } else {
                            showAlert("Upload Failed", "Server Error: ${response.errorBody()?.string() ?: "Unknown error"}")
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        showAlert("Upload Failed", t.message ?: "Unknown error")
                    }
                })
        } ?: showAlert("Error", "File could not be processed.")
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
