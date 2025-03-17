package com.example.pdftomp3.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.pdftomp3.AllfilesActivity
import com.example.pdftomp3.AudiofilesActivity
import com.example.pdftomp3.R
import com.example.pdftomp3.SearchActivity
import com.example.pdftomp3.UploadfilesActivity

class HomeFragment : Fragment() {

    var aud: LinearLayout? = null
    var fil: LinearLayout? = null
    var con: LinearLayout? = null
    var ser: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        aud = root.findViewById(R.id.hometoaudio)

        aud?.setOnClickListener {
            val intent = Intent(requireActivity(), AudiofilesActivity::class.java)
            startActivity(intent)
        }

        fil = root.findViewById(R.id.hometofiles)

        fil?.setOnClickListener {
            val intent = Intent(requireActivity(), AllfilesActivity::class.java)
            startActivity(intent)
        }

        con = root.findViewById(R.id.hometoupload)

        con?.setOnClickListener {
            val intent = Intent(requireActivity(), UploadfilesActivity::class.java)
            startActivity(intent)
        }

        ser = root.findViewById(R.id.hometosearch)

        ser?.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }
        return root
    }

}