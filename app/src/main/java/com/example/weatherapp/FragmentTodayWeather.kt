package com.example.weatherapp

import UnitViewModel
import WeatherData
import WeatherViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale



class FragmentTodayWeather : Fragment() {

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var weatherImageView: ImageView
    private lateinit var coordinatesTextView: TextView
    private lateinit var weatherTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var searchButton: Button
    private lateinit var searchEditText: TextInputEditText

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var unitViewModel: UnitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today_weather, container, false)

        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)


        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        cityTextView = view.findViewById(R.id.cityTextView)
        coordinatesTextView = view.findViewById(R.id.coordinatesTextView)
        weatherTextView = view.findViewById(R.id.weatherTextView)
        pressureTextView = view.findViewById(R.id.pressureTextView)
        timeTextView = view.findViewById(R.id.timeTextView)
        weatherImageView = view.findViewById(R.id.weatherImageView)

        searchButton = view.findViewById(R.id.searchButton)
        searchEditText = view.findViewById(R.id.searchEditText)

        searchButton.setOnClickListener {
            val newCity = searchEditText.text.toString()
            weatherViewModel.updateCity(newCity)
            val unit = unitViewModel.unit.value ?: "metric"
            weatherViewModel.fetchWeather(unit)
        }

        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
            val currentUnit = unit ?: "metric"
            weatherViewModel.fetchWeather(currentUnit)
        })

        weatherViewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            println("Weather Data Changed: $weatherData")
            displayWeatherData(weatherData)
        })

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeatherData(weatherData: WeatherData?) {
        if (weatherData != null) {
            cityTextView.text = weatherData.city
            if(unitViewModel.unit.value=="metric") temperatureTextView.text = "${weatherData.temperature.toInt()}°C"
            else temperatureTextView.text = "${weatherData.temperature.toInt()}K"
            coordinatesTextView.text = "Longitude: ${weatherData.longitude}, Latitude: ${weatherData.latitude}"
            weatherTextView.text = "Weather: ${weatherData.description}"
            pressureTextView.text = "Pressure: ${weatherData.pressure} hPa"
            timeTextView.text = "Local Time: ${weatherData.time}"

            val icon = getWeatherIcon(weatherData.description)
            weatherImageView.setImageResource(icon)
        } else {
            cityTextView.text = "City not found"
            temperatureTextView.text = "-"
            coordinatesTextView.text = "Coordinates: -"
            weatherTextView.text = "Weather: -"
            pressureTextView.text = "Pressure: -"
            timeTextView.text = "Local Time: -"
            weatherImageView.setImageResource(R.drawable.nodata)
        }
    }

    private fun getWeatherIcon(description: String): Int {
        return when (description.lowercase(Locale.getDefault())) {
            "clear sky" -> R.drawable.sun_and_cloud
            "few clouds", "scattered clouds", "broken clouds", "overcast clouds" -> R.drawable.cloud
            "shower rain", "rain" -> R.drawable.rain
            "thunderstorm" -> R.drawable.storm
            "snow" -> R.drawable.snow
            "mist" -> R.drawable.mist
            "light rain", "light intensity shower rain" -> R.drawable.little_rain
            else -> R.drawable.nodata
        }
    }
}



