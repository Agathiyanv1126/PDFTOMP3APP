package com.example.pdftomp3.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.pdftomp3.AboutActivity
import com.example.pdftomp3.AccountandsecurityActivity
import com.example.pdftomp3.AudiofilesActivity
import com.example.pdftomp3.HelpandsupportActivity
import com.example.pdftomp3.LogoutActivity
import com.example.pdftomp3.PrivacyActivity
import com.example.pdftomp3.R
import com.example.pdftomp3.diaplayActivity

class ProfileFragment : Fragment() {

    var editLay: LinearLayout?=null
    var DisplayLay: LinearLayout?=null
    var audiolist: LinearLayout?=null
    var logLay: LinearLayout?=null
    private lateinit var sharedPreferences: SharedPreferences
    var name=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        name= sharedPreferences.getInt("user_id",0)

        Log.i("userid11", name.toString())


        editLay = v.findViewById(R.id.edit)
        editLay?.setOnClickListener {
            val i = Intent(requireActivity(), AccountandsecurityActivity::class.java)
            startActivity(i)
        }

        DisplayLay = v.findViewById(R.id.display)
        DisplayLay?.setOnClickListener {
            val i = Intent(requireActivity(), diaplayActivity::class.java)
            startActivity(i)
        }

        audiolist = v.findViewById(R.id.audiolist)
        audiolist?.setOnClickListener {
            val i = Intent(requireActivity(), AudiofilesActivity::class.java)
            startActivity(i)
        }

        logLay = v.findViewById(R.id.profiletoLogout)
        logLay?.setOnClickListener {
            val i = Intent(requireActivity(), LogoutActivity::class.java)
            startActivity(i)
        }


        return v

    }

}