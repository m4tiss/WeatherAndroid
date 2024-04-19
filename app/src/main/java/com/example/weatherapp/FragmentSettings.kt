package com.example.weatherapp

import UnitViewModel
import WeatherViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager

class FragmentSettings : Fragment() {

    private lateinit var unitRadioGroup: RadioGroup
    private lateinit var kelvinRadioButton: RadioButton
    private lateinit var celsiusRadioButton: RadioButton
    private lateinit var unitViewModel: UnitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        unitRadioGroup = view.findViewById(R.id.unitRadioGroup)
        kelvinRadioButton = view.findViewById(R.id.kelvinRadioButton)
        celsiusRadioButton = view.findViewById(R.id.celsiusRadioButton)


        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)

        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
            if (unit == "standard") {
                kelvinRadioButton.isChecked = true
            } else {
                celsiusRadioButton.isChecked = true
            }
        })

        unitRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.kelvinRadioButton -> {
                    unitViewModel.setUnit("standard")
                }
                R.id.celsiusRadioButton -> {
                    unitViewModel.setUnit("metric")
                }
            }
        }


        return view
    }
}
