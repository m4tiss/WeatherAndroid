package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

class FragmentSettings : Fragment() {

    private lateinit var unitRadioGroup: RadioGroup
    private lateinit var kelvinRadioButton: RadioButton
    private lateinit var celsiusRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        unitRadioGroup = view.findViewById(R.id.unitRadioGroup)
        kelvinRadioButton = view.findViewById(R.id.kelvinRadioButton)
        celsiusRadioButton = view.findViewById(R.id.celsiusRadioButton)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val currentUnit = sharedPreferences.getString("temperatureUnit", "Kelvin")

        if (currentUnit == "Kelvin") {
            kelvinRadioButton.isChecked = true
        } else {
            celsiusRadioButton.isChecked = true
        }

        unitRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val editor = sharedPreferences.edit()
            when (checkedId) {
                R.id.kelvinRadioButton -> {
                    editor.putString("temperatureUnit", "Kelvin")
                }
                R.id.celsiusRadioButton -> {
                    editor.putString("temperatureUnit", "Celsius")
                }
            }
            editor.apply()
        }


        return view
    }
}
