package com.example.pdftomp3.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.pdftomp3.AboutActivity
import com.example.pdftomp3.AccountandsecurityActivity
import com.example.pdftomp3.HelpandsupportActivity
import com.example.pdftomp3.PrivacyActivity
import com.example.pdftomp3.R

class SettingsFragment : Fragment() {

    var accountLay:LinearLayout?=null
    var aboutLay:LinearLayout?=null
    var privacyLay:LinearLayout?=null
    var helpLay:LinearLayout?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        accountLay = v.findViewById(R.id.account)
        accountLay?.setOnClickListener {
            val i = Intent(requireActivity(),AccountandsecurityActivity::class.java)
            startActivity(i)
        }

        aboutLay = v.findViewById(R.id.about)
        aboutLay?.setOnClickListener {
            val i = Intent(requireActivity(),AboutActivity::class.java)
            startActivity(i)
        }

        privacyLay = v.findViewById(R.id.privacy)
        privacyLay?.setOnClickListener {
            val i = Intent(requireActivity(),PrivacyActivity::class.java)
            startActivity(i)
        }

        helpLay = v.findViewById(R.id.helpsup)
        helpLay?.setOnClickListener {
            val i = Intent(requireActivity(),HelpandsupportActivity::class.java)
            startActivity(i)
        }


        return v

    }

}