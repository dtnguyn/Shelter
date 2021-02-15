package com.nguyen.shelter.ui.main.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nguyen.shelter.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    companion object{
        const val SETTINGS_PREF = "APP_SETTINGS"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.notification_switch.isChecked = activity?.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE)?.getBoolean("notification", true) ?: true
        view.settings_back_button.setOnClickListener {
            findNavController().popBackStack()
        }

        view.notification_switch.setOnCheckedChangeListener { _, isChecked ->
            activity?.let {
                val sharedPreference =  it.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putBoolean("notification", isChecked)
                editor.apply()
            }
        }


        return view
    }


}