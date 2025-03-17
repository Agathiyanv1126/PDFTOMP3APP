package com.example.pdftomp3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdftomp3.api.RetrofitClient
import com.example.pdftomp3.models.AudioFile

class Audiofilesadapter(
    private var audioList: List<AudioFile>, // ✅ Store the main list
    private val context: Context
) : RecyclerView.Adapter<Audiofilesadapter.AudioViewHolder>() {

    private var filteredList: MutableList<AudioFile> = audioList.toMutableList() // ✅ Filtered list

    class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(R.id.fileName)
        val fileDate: TextView = view.findViewById(R.id.fileDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audiofiles, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFile = filteredList[position]
        holder.fileName.text = audioFile.title
        holder.fileDate.text = audioFile.date

        holder.itemView.setOnClickListener {
            val intent = Intent(context, musicplayerActivity::class.java)
            val url = "${RetrofitClient.BASE_URL}media/${audioFile.mp3File}"
            intent.putExtra("AUDIO_FILE_NAME", url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredList.size // ✅ Return filtered list size

    // ✅ Update the entire list (called from activity)
    fun updateList(newList: List<AudioFile>) {
        audioList = newList
        filteredList = audioList.toMutableList()
        notifyDataSetChanged()
    }

    // ✅ Filtering logic
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            audioList.toMutableList()
        } else {
            val lowerCaseQuery = query.lowercase()
            audioList.filter {
                it.title.lowercase().contains(lowerCaseQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
