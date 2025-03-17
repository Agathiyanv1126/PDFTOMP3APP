package com.example.pdftomp3

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.ui.AddFavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class musicplayerActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var bac: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageView
    private lateinit var pro: LinearLayout
    private lateinit var fav: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicplayer)

        // Initialize UI components
        pro = findViewById(R.id.musictoaudio)
        fav = findViewById(R.id.musictofav)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        seekBar = findViewById(R.id.seekBar)
        bac = findViewById(R.id.playertoback)

        bac.setOnClickListener { finish() }
        // Navigate to Audio Files Activity
        pro.setOnClickListener {
            startActivity(Intent(this, AudiofilesActivity::class.java))
        }

        // Handle Add to Favorite click
        fav.setOnClickListener {
            addFavorite(4, 2)  // Example user ID and MP3 ID
        }

        // Get the audio file path from intent
        val audioPath = intent.getStringExtra("AUDIO_FILE_NAME")
        if (audioPath.isNullOrEmpty()) {
            Toast.makeText(this, "No audio file found!", Toast.LENGTH_SHORT).show()
            return
        }

        setupMediaPlayer(audioPath)

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                mediaPlayer?.pause()
                btnPlayPause.setImageResource(R.drawable.play)
            } else {
                mediaPlayer?.start()
                btnPlayPause.setImageResource(R.drawable.pause)
                updateSeekBar()
            }
            isPlaying = !isPlaying
        }
    }

    // Set up the media player
    private fun setupMediaPlayer(audioPath: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                if (audioPath.startsWith("http")) {
                    setDataSource(audioPath) // Streaming file
                } else {
                    setDataSource(applicationContext, Uri.parse(audioPath)) // Local file
                }
                prepareAsync()
                setOnPreparedListener {
                    seekBar.max = duration
                    Toast.makeText(this@musicplayerActivity, "Audio Ready!", Toast.LENGTH_SHORT).show()
                }
                setOnCompletionListener {
                    btnPlayPause.setImageResource(R.drawable.play)
                    this@musicplayerActivity.isPlaying = false
                }
            } catch (e: Exception) {
                Log.e("AUDIO_PLAYER", "Error setting data source", e)
                Toast.makeText(this@musicplayerActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Update SeekBar progress
    private fun updateSeekBar() {
        Thread {
            while (mediaPlayer != null) {
                try {
                    runOnUiThread {
                        seekBar.progress = mediaPlayer?.currentPosition ?: 0
                    }
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    // API call to add a favorite MP3
    private fun addFavorite(userId: Int, mp3Id: Int) {
        RetrofitClient.instance.addFavorite(userId, mp3Id).enqueue(object : Callback<AddFavoriteResponse> {
            override fun onResponse(call: Call<AddFavoriteResponse>, response: Response<AddFavoriteResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.Status) {
                        Toast.makeText(this@musicplayerActivity, "Favorite Added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@musicplayerActivity, "Failed to add favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@musicplayerActivity, "Response Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddFavoriteResponse>, t: Throwable) {
                Toast.makeText(this@musicplayerActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Release media player resources when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
