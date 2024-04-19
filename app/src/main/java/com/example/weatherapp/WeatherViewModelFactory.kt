package com.example.weatherapp

import UnitViewModel
import WeatherViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(private val application: Application, private val unitViewModel: UnitViewModel) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(application, unitViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
