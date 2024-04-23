package com.example.weatherapp

import NetworkConnection
import UnitViewModel
import WeatherViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FragmentSettings : Fragment() {

    private lateinit var unitRadioGroup: RadioGroup
    private lateinit var kelvinRadioButton: RadioButton
    private lateinit var celsiusRadioButton: RadioButton
    private lateinit var refreshTime: Spinner
    private lateinit var unitViewModel: UnitViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        unitRadioGroup = view.findViewById(R.id.unitRadioGroup)
        kelvinRadioButton = view.findViewById(R.id.kelvinRadioButton)
        celsiusRadioButton = view.findViewById(R.id.celsiusRadioButton)
        refreshTime = view.findViewById(R.id.refreshTime)

        val refreshOptions = listOf("5 seconds", "10 seconds", "30 seconds", "1 minute")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, refreshOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        refreshTime.adapter = adapter

        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
            if (unit == "standard") {
                kelvinRadioButton.isChecked = true
            } else {
                celsiusRadioButton.isChecked = true
            }
        })

        unitRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val networkConnection = NetworkConnection(requireContext())

            when (checkedId) {
                R.id.kelvinRadioButton -> {
                    if(unitViewModel.unit.value!="standard"){
                    unitViewModel.setUnit("standard")
                    if (!networkConnection.isNetworkAvailable()) {
                        val currentTemperature = weatherViewModel.weatherData.value?.temperature ?: 0.0
                        val newTemperature = currentTemperature + 273
                        weatherViewModel.updateTemperature(newTemperature)
                        }
                    }
                }
                R.id.celsiusRadioButton -> {
                    if(unitViewModel.unit.value!="metric") {
                        unitViewModel.setUnit("metric")
                        if (!networkConnection.isNetworkAvailable()) {
                            val currentTemperature =
                                weatherViewModel.weatherData.value?.temperature ?: 0.0
                            val newTemperature = currentTemperature - 273
                            weatherViewModel.updateTemperature(newTemperature)
                        }
                    }
                }
            }
        }

        refreshTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = refreshOptions[position]
                val fetchIntervalMillis = when (selectedOption) {
                    "5 seconds" -> 5000L
                    "10 seconds" -> 10000L
                    "30 seconds" -> 30000L
                    "1 minute" -> 60000L
                    else -> 0L
                }

                weatherViewModel.setFetchIntervalMillis(fetchIntervalMillis)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        return view
    }
}
