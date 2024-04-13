package com.example.weatherapp

import WeatherData
import WeatherViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FragmentAdvancedInfo : Fragment() {

    private lateinit var windSpeedTextView: TextView
    private lateinit var windDegTextView: TextView
    private lateinit var windImageView: ImageView
    private lateinit var visibilityTextView: TextView
    private lateinit var visibilityImageView: ImageView
    private lateinit var humidityTextView: TextView
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_advanced_info, container, false)


        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        windSpeedTextView = view.findViewById(R.id.windSpeedTextView)
        windDegTextView = view.findViewById(R.id.windDegTextView)
        windImageView = view.findViewById(R.id.windImageView)
        visibilityTextView = view.findViewById(R.id.visibilityTextView)
        visibilityImageView = view.findViewById(R.id.visibilityImageView)
        humidityTextView = view.findViewById(R.id.humidityTextView)

        weatherViewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            displayWeatherData(weatherData)
        })

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeatherData(weatherData: WeatherData?) {
        if (weatherData != null) {
            windSpeedTextView.text = "Speed: ${weatherData.windSpeed}"
            windDegTextView.text = "Degree: ${weatherData.windDeg}"
            humidityTextView.text = "Humidity: ${weatherData.humidity}%"

            val visibility = weatherData.visibility
            visibilityTextView.text = "Visibility\n$visibility"

            val iconWind = if (weatherData.windSpeed > 10) R.drawable.wind else R.drawable.nowind
            windImageView.setImageResource(iconWind)

            val iconVisibility = if (weatherData.visibility <= 2000) R.drawable.novisibility else R.drawable.visibility
            visibilityImageView.setImageResource(iconVisibility)
        } else {
            windSpeedTextView.text = "Speed: -"
            windDegTextView.text = "Degree: -"
            humidityTextView.text = "Humidity: -"
            visibilityTextView.text = "Visibility\n-"

            windImageView.setImageResource(R.drawable.nodata)
            visibilityImageView.setImageResource(R.drawable.nodata)
        }
    }
}
