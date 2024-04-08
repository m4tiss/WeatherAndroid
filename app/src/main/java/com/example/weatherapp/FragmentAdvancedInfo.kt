package com.example.weatherapp

import CityViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class FragmentAdvancedInfo : Fragment() {

    private lateinit var windSpeedTextView: TextView
    private lateinit var windDegTextView: TextView
    private lateinit var windImageView: ImageView

    private lateinit var visibilityTextView: TextView
    private lateinit var visibilityImageView: ImageView

    private lateinit var humidityTextView: TextView

    private lateinit var cityViewModel: CityViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_advanced_info, container, false)

        cityViewModel = ViewModelProvider(requireActivity()).get(CityViewModel::class.java)


        windSpeedTextView = view.findViewById(R.id.windSpeedTextView)
        windDegTextView = view.findViewById(R.id.windDegTextView)
        windImageView = view.findViewById(R.id.windImageView)

        visibilityTextView = view.findViewById(R.id.visibilityTextView)
        visibilityImageView = view.findViewById(R.id.visibilityImageView)

        humidityTextView = view.findViewById(R.id.humidityTextView)


        cityViewModel.city.observe(viewLifecycleOwner, Observer { city ->
            WeatherTask().execute()
        })

        WeatherTask().execute()

        return view
    }

    inner class WeatherTask {

        private var apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        private var apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=${cityViewModel.city.value}&appid=$apiKey"

        fun execute() {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        URL(apiUrl).readText()
                    }
                    handleResponse(response)
                } catch (e: IOException) {
                    windSpeedTextView.text = "Speed: -"
                    windDegTextView.text = "Degree: -"
                    visibilityTextView.text = "Visibility\n-"
                    humidityTextView.text = "Humidity: -"
                    windImageView.setImageResource(R.drawable.nodata)
                    visibilityImageView.setImageResource(R.drawable.nodata)
                }
            }
        }

        private fun handleResponse(response: String) {
            try {
                val jsonObj = JSONObject(response)

                val main = jsonObj.getJSONObject("main")

                val wind = jsonObj.getJSONObject("wind")
                val speed = wind.getDouble("speed")
                val deg = wind.getDouble("deg")

                val humidity = main.getInt("humidity")

                val visibility = jsonObj.getInt("visibility")


                val formattedWindSpeed = "Speed: $speed"
                val formattedWindDegree = "Degree: $deg"
                val formattedHumidity = "Humidity: $humidity%"

                val formattedVisibility = "Visibility\n$visibility"


                windSpeedTextView.text = formattedWindSpeed
                windDegTextView.text = formattedWindDegree
                humidityTextView.text = formattedHumidity
                visibilityTextView.text = formattedVisibility

                val icon_wind = when {
                    speed > 10 -> R.drawable.wind
                    speed <= 10 -> R.drawable.nowind
                    else -> R.drawable.nodata
                }

                windImageView.setImageResource(icon_wind)

                val icon_visibility = when {
                    visibility <= 2000 -> R.drawable.novisibility
                    visibility > 2000 -> R.drawable.visibility
                    else -> R.drawable.nodata
                }

                visibilityImageView.setImageResource(icon_visibility)

            } catch (e: Exception) {
                windSpeedTextView.text = "Speed: -"
                windDegTextView.text = "Degree: -"
                visibilityTextView.text = "Visibility\n-"
                humidityTextView.text = "Humidity: -"
                windImageView.setImageResource(R.drawable.nodata)
                visibilityImageView.setImageResource(R.drawable.nodata)

            }
        }


    }
}
