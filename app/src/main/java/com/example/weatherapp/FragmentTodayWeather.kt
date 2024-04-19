package com.example.weatherapp

import FavouritesCitiesViewModel
import NetworkConnection
import UnitViewModel
import WeatherData
import WeatherDataForecast
import WeatherViewModel
import android.annotation.SuppressLint
import android.app.Application
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
import android.widget.Toast


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
    private lateinit var heartImageView: ImageView
    private lateinit var refreshIcon: ImageView

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherForecastViewModel: WeatherForecastViewModel
    private lateinit var unitViewModel: UnitViewModel
    private lateinit var favouritesCitiesViewModel: FavouritesCitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today_weather, container, false)

        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)
        weatherViewModel = ViewModelProvider(requireActivity(), WeatherViewModelFactory(requireActivity().application, unitViewModel)).get(WeatherViewModel::class.java)
        val factory = WeatherForecastViewModelFactory(requireActivity().application)
        weatherForecastViewModel = ViewModelProvider(requireActivity(), factory).get(WeatherForecastViewModel::class.java)


        favouritesCitiesViewModel = ViewModelProvider(requireActivity()).get(FavouritesCitiesViewModel::class.java)


        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        cityTextView = view.findViewById(R.id.cityTextView)
        coordinatesTextView = view.findViewById(R.id.coordinatesTextView)
        weatherTextView = view.findViewById(R.id.weatherTextView)
        pressureTextView = view.findViewById(R.id.pressureTextView)
        timeTextView = view.findViewById(R.id.timeTextView)
        weatherImageView = view.findViewById(R.id.weatherImageView)
        heartImageView =  view.findViewById(R.id.heartIcon)
        refreshIcon =  view.findViewById(R.id.refreshIcon)

        searchButton = view.findViewById(R.id.searchButton)
        searchEditText = view.findViewById(R.id.searchEditText)
        favouritesCitiesViewModel.loadFavouriteCities(requireContext())
        unitViewModel.setUnit("metric")

        searchButton.setOnClickListener {
            val newCity = searchEditText.text.toString()
            weatherViewModel.updateCity(newCity)
            val unit = unitViewModel.unit.value ?: "metric"
            weatherViewModel.fetchWeather()
            weatherForecastViewModel.fetchWeatherForecast(newCity,unit)
        }
        refreshIcon.setOnClickListener {
            val unit = unitViewModel.unit.value ?: "metric"
            val currentCity = weatherViewModel.weatherData.value?.city ?: "Warsaw"
            weatherViewModel.fetchWeather()
            weatherForecastViewModel.fetchWeatherForecast(currentCity,unit)
        }

        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
            val networkConnection = NetworkConnection(requireContext())
            if (networkConnection.isNetworkAvailable()) {
                val currentUnit = unit ?: "metric"
                println(weatherViewModel.weatherData.value?.temperature)
                val currentCity = weatherViewModel.weatherData.value?.city ?: "Warsaw"
                weatherViewModel.fetchWeather()
                weatherForecastViewModel.fetchWeatherForecast(currentCity,currentUnit)
            }
        })

        weatherViewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            displayWeatherData(weatherData)
        })

        heartImageView.setOnClickListener {
            val currentCity = cityTextView.text.toString()
            if (isCityInFavourites(currentCity)) {
                favouritesCitiesViewModel.removeFavouriteCity(requireContext(),currentCity)
                weatherViewModel.weatherData.value?.let { weatherData ->
                    weatherViewModel.deleteWeatherDataFile(currentCity)
                }
                weatherForecastViewModel.weatherForecast.value?.let{ weatherDataForecast ->
                    weatherForecastViewModel.deleteWeatherForecastFile(currentCity)
                }

            } else {
                favouritesCitiesViewModel.addFavouriteCity(requireContext(),currentCity)
                weatherViewModel.weatherData.value?.let { weatherData ->
                    weatherViewModel.saveWeatherDataToFile(currentCity, weatherData)
                }
                weatherForecastViewModel.weatherForecast.value?.let{ weatherDataForecast ->
                    weatherForecastViewModel.saveWeatherForecastToFile(currentCity)
                }
                Toast.makeText(requireContext(), "Miasto zostało dodane!", Toast.LENGTH_SHORT).show()
            }
        }
        favouritesCitiesViewModel.favouritesCities.observe(viewLifecycleOwner, Observer { favouriteCities ->
            updateHeartIcon(cityTextView.text.toString())
        })

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeatherData(weatherData: WeatherData?) {
        if (weatherData != null) {
            cityTextView.text = weatherData.city
            updateHeartIcon(weatherData.city)
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

    private fun isCityInFavourites(city: String): Boolean {
        return favouritesCitiesViewModel.favouritesCities.value?.contains(city) ?: false
    }

    private fun updateHeartIcon(city: String) {
        if (isCityInFavourites(city)) {
            heartImageView.setImageResource(R.drawable.heart)
        } else {
            heartImageView.setImageResource(R.drawable.noheart)
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



